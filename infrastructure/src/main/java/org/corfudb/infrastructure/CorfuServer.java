package org.corfudb.infrastructure;

import static org.corfudb.util.NetworkUtils.getAddressFromInterfaceName;

import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.WHITE;
import static org.fusesource.jansi.Ansi.ansi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.ImmutableMap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import java.io.File;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.corfudb.protocols.wireprotocol.NettyCorfuMessageDecoder;
import org.corfudb.protocols.wireprotocol.NettyCorfuMessageEncoder;
import org.corfudb.runtime.exceptions.unrecoverable.UnrecoverableCorfuError;
import org.corfudb.security.sasl.plaintext.PlainTextSaslNettyServer;
import org.corfudb.security.tls.SslContextConstructor;
import org.corfudb.util.GitRepositoryState;
import org.corfudb.util.Version;
import org.docopt.Docopt;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.LoggerFactory;


/**
 * This is the new Corfu server single-process executable.
 *
 * <p>The command line options are documented in the USAGE variable.
 *
 * <p>Created by mwei on 11/30/15.
 */

@Slf4j
public class CorfuServer implements AutoCloseable {
    /**
     * This string defines the command line arguments,
     * in the docopt DSL (see http://docopt.org) for the executable.
     * It also serves as the documentation for the executable.
     *
     * <p>Unfortunately, Java doesn't support multi-line string literals,
     * so you must concatenate strings and terminate with newlines.
     *
     * <p>Note that the java implementation of docopt has a strange requirement
     * that each option must be preceded with a space.
     */
    private static final String USAGE =
        "Corfu Server, the server for the Corfu Infrastructure.\n"
            + "\n"
            + "Usage:\n"
            + "\tcorfu_server (-l <path>|-m) [-ns] [-a <address>|-q <interface-name>] "
            + "[-t <token>] [-c <ratio>] [-d <level>] [-p <seconds>] [-M <address>:<port>] "
            + "[-e [-u <keystore> -f <keystore_password_file>] [-r <truststore> -w "
            + "<truststore_password_file>] [-b] [-g -o <username_file> -j <password_file>] "
            + "[-k <seqcache>] [-T <threads>] [-i <channel-implementation>] [-H <seconds>] "
            + "[-I <cluster-id>] [-x <ciphers>] [-z <tls-protocols>]] [-P <prefix>]"
            + " [--agent] <port>\n"
            + "\n"
            + "Options:\n"
            + " -l <path>, --log-path=<path>                                             "
            + "              Set the path to the storage file for the log unit.\n"
            + " -s, --single                                                             "
            + "              Deploy a single-node configuration.\n"
            + " -I <cluster-id>, --cluster-id=<cluster-id>"
            + "              For a single node configuration the cluster id to use in UUID,"
            + "              base64 format, or auto to randomly generate [default: auto].\n"
            + " -T <threads>, --Threads=<threads>                                        "
            + "              Number of corfu server worker threads, or 0 to use 2x the "
            + "              number of available processors [default: 0].\n"
            + " -P <prefix> --Prefix=<prefix>"
            + "              The prefix to use for threads (useful for debugging multiple"
            + "              servers) [default: ]."
            + "                                                                          "
            + "              The server will be bootstrapped with a simple one-unit layout."
            + "\n -a <address>, --address=<address>                                      "
            + "                IP address for the server router to bind to and to "
            + "advertise to external clients.\n"
            + " -q <interface-name>, --network-interface=<interface-name>                "
            + "              The name of the network interface.\n"
            + " -i <channel-implementation>, --implementation <channel-implementation>   "
            + "              The type of channel to use (auto, nio, epoll, kqueue)"
            + "[default: nio].\n"
            + " -m, --memory                                                             "
            + "              Run the unit in-memory (non-persistent).\n"
            + "                                                                          "
            + "              Data will be lost when the server exits!\n"
            + " -c <ratio>, --cache-heap-ratio=<ratio>                                   "
            + "              The ratio of jvm max heap size we will use for the the "
            + "in-memory cache to serve requests from -\n"
            + "                                                                          "
            + "              (e.g. ratio = 0.5 means the cache size will be 0.5 * jvm max "
            + "heap size\n"
            + "                                                                          "
            + "              If there is no log, then this will be the size of the log unit"
            + "\n                                                                        "
            + "                evicted entries will be auto-trimmed. [default: 0.5].\n"
            + " -H <seconds>, --HandshakeTimeout=<sceonds>                               "
            + "              Handshake timeout in seconds [default: 10].\n               "
            + " -t <token>, --initial-token=<token>                                      "
            + "              The first token the sequencer will issue, or -1 to recover\n"
            + "                                                                          "
            + "              from the log. [default: -1].\n                              "
            + "                                                                          "
            + " -k <seqcache>, --sequencer-cache-size=<seqcache>                         "
            + "               The size of the sequencer's cache. [default: 250000].\n    "
            + " -p <seconds>, --compact=<seconds>                                        "
            + "              The rate the log unit should compact entries (find the,\n"
            + "                                                                          "
            + "              contiguous tail) in seconds [default: 60].\n"
            + " -d <level>, --log-level=<level>                                          "
            + "              Set the logging level, valid levels are: \n"
            + "                                                                          "
            + "              ALL,ERROR,WARN,INFO,DEBUG,TRACE,OFF [default: INFO].\n"
            + " -M <address>:<port>, --management-server=<address>:<port>                "
            + "              Layout endpoint to seed Management Server\n"
            + " -n, --no-verify                                                          "
            + "              Disable checksum computation and verification.\n"
            + " -e, --enable-tls                                                         "
            + "              Enable TLS.\n"
            + " -u <keystore>, --keystore=<keystore>                                     "
            + "              Path to the key store.\n"
            + " -f <keystore_password_file>, "
            + "--keystore-password-file=<keystore_password_file>         Path to the file "
            + "containing the key store password.\n"
            + " -b, --enable-tls-mutual-auth                                             "
            + "              Enable TLS mutual authentication.\n"
            + " -r <truststore>, --truststore=<truststore>                               "
            + "              Path to the trust store.\n"
            + " -w <truststore_password_file>, "
            + "--truststore-password-file=<truststore_password_file>   Path to the file "
            + "containing the trust store password.\n"
            + " -g, --enable-sasl-plain-text-auth                                        "
            + "              Enable SASL Plain Text Authentication.\n"
            + " -o <username_file>, --sasl-plain-text-username-file=<username_file>      "
            + "              Path to the file containing the username for SASL Plain Text "
            + "Authentication.\n"
            + " -j <password_file>, --sasl-plain-text-password-file=<password_file>      "
            + "              Path to the file containing the password for SASL Plain Text "
            + "Authentication.\n"
            + " -x <ciphers>, --tls-ciphers=<ciphers>                                    "
            + "              Comma separated list of TLS ciphers to use.\n"
            + "                                                                          "
            + "              [default: TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256].\n"
            + " -z <tls-protocols>, --tls-protocols=<tls-protocols>                      "
            + "              Comma separated list of TLS protocols to use.\n"
            + "                                                                          "
            + "              [default: TLSv1.1,TLSv1.2].\n"
            + " --agent      Run with byteman agent to enable runtime code injection.\n  "
            + " -h, --help                                                               "
            + "              Show this screen\n"
            + " --version                                                                "
            + "              Show version\n";

    private static volatile CorfuServer ACTIVE_SERVER;

    private static volatile boolean SHUTDOWN_SERVER = false;
    private static volatile boolean CLEANUP_SERVER = false;

    /**
     * Main program entry point.
     *
     * @param args command line argument strings
     */
    public static void main(String[] args) {

        // Parse the options given, using docopt.
        Map<String, Object> opts = new Docopt(USAGE)
            .withVersion(GitRepositoryState.getRepositoryState().describe)
            .parse(args);
        // Print a nice welcome message.
        AnsiConsole.systemInstall();
        printStartupMsg(opts);

        // Pick the correct logging level before outputting error messages.
        final Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        final Level level = Level.toLevel(((String) opts.get("--log-level")).toUpperCase());
        root.setLevel(level);

        log.debug("Started with arguments: " + opts);

        // Bind to all interfaces only if no address or interface specified by the user.
        final boolean bindToAllInterfaces;
        // Fetch the address if given a network interface.
        if (opts.get("--network-interface") != null) {
            opts.put("--address",
                getAddressFromInterfaceName((String) opts.get("--network-interface")));
            bindToAllInterfaces = false;
        } else if (opts.get("--address") == null) {
            // Default the address to localhost and set the bind to all interfaces flag to true,
            // if the address and interface is not specified.
            bindToAllInterfaces = true;
            opts.put("--address", "localhost");
        } else {
            // Address is specified by the user.
            bindToAllInterfaces = false;
        }

        // Create the service directory if it does not exist.
        if (!(Boolean) opts.get("--memory")) {
            File serviceDir = new File((String) opts.get("--log-path"));

            if (!serviceDir.isDirectory()) {
                log.error("Service directory {} does not point to a directory. Aborting.",
                    serviceDir);
                throw new UnrecoverableCorfuError("Service directory must be a directory!");
            } else {
                String corfuServiceDirPath = serviceDir.getAbsolutePath()
                    + File.separator
                    + "corfu";
                File corfuServiceDir = new File(corfuServiceDirPath);
                // Update the new path with the dedicated child service directory.
                opts.put("--log-path", corfuServiceDirPath);
                if (!corfuServiceDir.exists() && corfuServiceDir.mkdirs()) {
                    log.info("Created new service directory at {}.", corfuServiceDir);
                }
            }
        }

        // Register shutdown handler
        Thread shutdownThread = new Thread(CorfuServer::cleanShutdown);
        shutdownThread.setName("ShutdownThread");
        Runtime.getRuntime().addShutdownHook(shutdownThread);

        while (!SHUTDOWN_SERVER) {
            final ServerContext sc = new ServerContext(opts);
            try (CorfuServer corfuServer = new CorfuServer(sc)) {
                ACTIVE_SERVER = corfuServer;
                corfuServer.start().channel().closeFuture().syncUninterruptibly();
            }

            if (CLEANUP_SERVER) {
                log.warn("main: cleanup reqeusted, DELETE server data files");
                if (!sc.getServerConfig(Boolean.class, "--memory")) {
                    File serviceDir = new File(sc.getServerConfig(String.class, "--log-path"));
                    try {
                        FileUtils.cleanDirectory(serviceDir);
                    } catch (IOException ioe) {
                        throw new UnrecoverableCorfuError(ioe);
                    }
                }
                CLEANUP_SERVER = false;
                log.warn("main: cleanup completed, expect clean startup");
            }
        }

        log.info("main: Server exiting due to shutdown");
    }


    @Getter
    private final ServerContext serverContext;

    @Getter
    private final Map<Class<? extends AbstractServer>, AbstractServer> serverMap;

    @Getter
    private final NettyServerRouter router;

    private volatile boolean shutdown = false;

    private ChannelFuture bindFuture;

    public CorfuServer(@Nonnull ServerContext serverContext) {
        this(serverContext,
            ImmutableMap.<Class<? extends AbstractServer>, AbstractServer>builder()
                .put(BaseServer.class, new BaseServer(serverContext))
                .put(SequencerServer.class, new SequencerServer(serverContext))
                .put(LayoutServer.class, new LayoutServer(serverContext))
                .put(LogUnitServer.class, new LogUnitServer(serverContext))
                .put(ManagementServer.class, new ManagementServer(serverContext))
                .build()
            );
    }

    public CorfuServer(@Nonnull ServerContext serverContext,
                       @Nonnull Map<Class<? extends AbstractServer>, AbstractServer> serverMap) {
        this.serverContext = serverContext;
        this.serverMap = serverMap;
        router = new NettyServerRouter(serverMap.values(), serverContext);
    }

    public ChannelFuture start() {
        bindFuture = startAndListen(serverContext.getBossGroup(),
            serverContext.getWorkerGroup(),
            b -> configureBootstrapOptions(serverContext, b),
            serverContext,
            router,
            serverContext.getNodeLocator().getBindingSocketAddress());

        return bindFuture.syncUninterruptibly();
    }

    @Override
    public synchronized void close() {
        if (!shutdown) {
            log.info("close: Shutting down Corfu server and cleaning resources");
            shutdown = true;
            serverContext.close();
            bindFuture.channel().close().syncUninterruptibly();

            // A executor service to create the shutdown threads
            // plus name the threads correctly.
            final ExecutorService shutdownService =
                Executors.newFixedThreadPool(serverMap.size());

            // Turn into a list of futures on the shutdown, returning
            // generating a log message to inform of the result.
            CompletableFuture[] shutdownFutures = serverMap.values().stream()
                .map(s -> CompletableFuture.runAsync(() -> {
                    try {
                        Thread.currentThread().setName(s.getClass().getSimpleName()
                            + "-shutdown");
                        log.info("close: Shutting down {}",
                            s.getClass().getSimpleName());
                        s.shutdown();
                        log.info("close: Cleanly shutdown {}",
                            s.getClass().getSimpleName());
                    } catch (Exception e) {
                        log.error("close: Failed to cleanly shutdown {}",
                            s.getClass().getSimpleName(), e);
                    }
                }, shutdownService))
                .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(shutdownFutures).join();
            shutdownService.shutdown();
            router.shutdown();
            log.info("close: Server shutdown and resources released");
        } else {
            log.trace("close: Server already shutdown");
        }
    }

    /** Get the requested Corfu server.
     *
     * @param serverClass
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public @Nonnull <T extends AbstractServer> T getServer(@Nonnull Class<T> serverClass) {
        T server = (T) serverMap.get(serverClass);
        if (server == null) {
            throw new UnrecoverableCorfuError("Server does not exist");
        }
        return server;
    }

    /** A functional interface for receiving and configuring a {@link ServerBootstrap}.
     */
    @FunctionalInterface
    public interface BootstrapConfigurer {

        /** Configure a {@link ServerBootstrap}.
         *
         * @param serverBootstrap   The {@link ServerBootstrap} to configure.
         */
        void configure(ServerBootstrap serverBootstrap);
    }


    /** Start the Corfu server and bind it to the given {@code port} using the provided
     * {@code channelType}. It is the callers' responsibility to shutdown the
     * {@link EventLoopGroup}s. For implementations which listen on multiple ports,
     * {@link EventLoopGroup}s may be reused.
     *
     * @param bossGroup             The "boss" {@link EventLoopGroup} which services incoming
     *                              connections.
     * @param workerGroup           The "worker" {@link EventLoopGroup} which services incoming
     *                              requests.
     * @param bootstrapConfigurer   A {@link BootstrapConfigurer} which will receive the
     *                              {@link ServerBootstrap} to set options.
     * @param context               A {@link ServerContext} which will be used to configure the
     *                              server.
     * @param router                A {@link NettyServerRouter} which will process incoming
     *                              messages.
     * @param address               The {@link SocketAddress} the {@link ServerChannel}
     *                              will be created on.
     * @return                      A {@link ChannelFuture} which can be used to wait for the server
     *                              to be shutdown.
     */
    public ChannelFuture startAndListen(@Nonnull EventLoopGroup bossGroup,
        @Nonnull EventLoopGroup workerGroup,
        @Nonnull BootstrapConfigurer bootstrapConfigurer,
        @Nonnull ServerContext context,
        @Nonnull NettyServerRouter router,
        @Nonnull SocketAddress address) {

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
            .channel(context.getChannelImplementation().getServerChannelClass());
        bootstrapConfigurer.configure(bootstrap);

        bootstrap.childHandler(getServerChannelInitializer(context, router));
        return bootstrap.bind(address);
    }

    /** Configure server bootstrap per-channel options, such as TCP options, etc.
     *
     * @param context       The {@link ServerContext} to use.
     * @param bootstrap     The {@link ServerBootstrap} to be configured.
     */
    public static void configureBootstrapOptions(@Nonnull ServerContext context,
        @Nonnull ServerBootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_BACKLOG, 100)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }


    /** Obtain a {@link ChannelInitializer} which initializes the channel pipeline
     *  for a new {@link ServerChannel}.
     *
     * @param context   The {@link ServerContext} to use.
     * @param router    The {@link NettyServerRouter} to initialize the channel with.
     * @return          A {@link ChannelInitializer} to intialize the channel.
     */
    private static ChannelInitializer getServerChannelInitializer(@Nonnull ServerContext context,
        @Nonnull NettyServerRouter router) {
        // Security variables
        final SslContext sslContext;
        final String[] enabledTlsProtocols;
        final String[] enabledTlsCipherSuites;

        // Security Initialization
        Boolean tlsEnabled = context.getServerConfig(Boolean.class, "--enable-tls");
        Boolean tlsMutualAuthEnabled = context.getServerConfig(Boolean.class,
            "--enable-tls-mutual-auth");
        if (tlsEnabled) {
            // Get the TLS cipher suites to enable
            String ciphs = context.getServerConfig(String.class, "--tls-ciphers");
            if (ciphs != null) {
                List<String> ciphers = Pattern.compile(",")
                    .splitAsStream(ciphs)
                    .map(String::trim)
                    .collect(Collectors.toList());
                enabledTlsCipherSuites = ciphers.toArray(new String[ciphers.size()]);
            } else {
                enabledTlsCipherSuites = new String[]{};
            }

            // Get the TLS protocols to enable
            String protos = context.getServerConfig(String.class, "--tls-protocols");
            if (protos != null) {
                List<String> protocols = Pattern.compile(",")
                    .splitAsStream(protos)
                    .map(String::trim)
                    .collect(Collectors.toList());
                enabledTlsProtocols = protocols.toArray(new String[protocols.size()]);
            } else {
                enabledTlsProtocols = new String[]{};
            }


            try {
                sslContext = SslContextConstructor.constructSslContext(true,
                    context.getServerConfig(String.class, "--keystore"),
                    context.getServerConfig(String.class, "--keystore-password-file"),
                    context.getServerConfig(String.class, "--truststore"),
                    context.getServerConfig(String.class,
                        "--truststore-password-file"));
            } catch (SSLException e) {
                log.error("Could not build the SSL context", e);
                throw new UnrecoverableCorfuError("Couldn't build the SSL context", e);
            }
        } else {
            enabledTlsCipherSuites = new String[]{};
            enabledTlsProtocols = new String[]{};
            sslContext = null;
        }

        Boolean saslPlainTextAuth = context.getServerConfig(Boolean.class,
            "--enable-sasl-plain-text-auth");

        // Generate the initializer.
        return new ChannelInitializer() {
            @Override
            protected void initChannel(@Nonnull Channel ch) throws Exception {
                // If TLS is enabled, setup the encryption pipeline.
                if (tlsEnabled) {
                    SSLEngine engine = sslContext.newEngine(ch.alloc());
                    engine.setEnabledCipherSuites(enabledTlsCipherSuites);
                    engine.setEnabledProtocols(enabledTlsProtocols);
                    if (tlsMutualAuthEnabled) {
                        engine.setNeedClientAuth(true);
                    }
                    ch.pipeline().addLast("ssl", new SslHandler(engine));
                }
                // Add/parse a length field
                ch.pipeline().addLast(new LengthFieldPrepender(4));
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer
                    .MAX_VALUE, 0, 4,
                    0, 4));
                // If SASL authentication is requested, perform a SASL plain-text auth.
                if (saslPlainTextAuth) {
                    ch.pipeline().addLast("sasl/plain-text", new
                        PlainTextSaslNettyServer());
                }
                // Transform the framed message into a Corfu message.
                ch.pipeline().addLast(new NettyCorfuMessageDecoder());
                ch.pipeline().addLast(new NettyCorfuMessageEncoder());
                ch.pipeline().addLast(new ServerHandshakeHandler(context.getNodeId(),
                    Version.getVersionString() + "("
                        + GitRepositoryState.getRepositoryState().commitIdAbbrev + ")",
                    context.getServerConfig(String.class, "--HandshakeTimeout")));
                // Route the message to the server class.
                ch.pipeline().addLast(router);
            }
        };
    }

    /**
     * Cleanly shuts down the server and restarts.
     *
     * @param serverContext Server Context.
     * @param resetData     Resets and clears all data if True.
     */
    public static void restartServer(ServerContext serverContext, boolean resetData) {

        final Map<String, Object> opts = serverContext.getServerConfig();

        if (resetData) {
            CLEANUP_SERVER = true;
        }

        log.info("RestartServer: Shutting down corfu server");
        ACTIVE_SERVER.close();

        log.info("RestartServer: Starting corfu server");
    }

    /**
     * Attempt to cleanly shutdown all the servers.
     */
    public static void cleanShutdown() {
        log.info("CleanShutdown: Starting Cleanup.");
        SHUTDOWN_SERVER = true;
        ACTIVE_SERVER.close();
    }

    /**
     * Print the corfu logo.
     */
    private static void printLogo() {
        println(ansi().fg(WHITE).toString());
        println("▄████████  ▄██████▄     ▄████████    ▄████████ ███    █▄");
        println("███    ███ ███    ███   ███    ███   ███    ██████    ███");
        println("███    █▀  ███    ███   ███    ███   ███    █▀ ███    ███");
        println("███        ███    ███  ▄███▄▄▄▄██▀  ▄███▄▄▄    ███    ███");
        println("███        ███    ███ ▀▀███▀▀▀▀▀   ▀▀███▀▀▀    ███    ███");
        println("███    █▄  ███    ███ ▀███████████   ███       ███    ███");
        println("███    ███ ███    ███   ███    ███   ███       ███    ███");
        println("████████▀   ▀██████▀    ███    ███   ███       ████████▀ ");
        println("                        ███    ███");
        println(ansi().reset().toString());
    }

    /** Print an object to the console, followed by a newline.
     *  Call this method instead of calling System.out.println().
     *
     * @param line  The object to print.
     */
    @SuppressWarnings("checkstyle:printLine")
    private static void println(Object line) {
        System.out.println(line);
    }

    /**
     * Print the welcome message, logo and the arguments.
     *
     * @param opts Arguments.
     */
    private static void printStartupMsg(Map<String, Object> opts) {
        printLogo();
        int port = Integer.parseInt((String) opts.get("<port>"));
        println(ansi().a("Welcome to ").fg(RED).a("CORFU ").fg(MAGENTA).a("SERVER").reset());
        println(ansi().a("Version ").a(Version.getVersionString()).a(" (").fg(BLUE)
            .a(GitRepositoryState.getRepositoryState().commitIdAbbrev).reset().a(")"));
        println(ansi().a("Serving on port ").fg(WHITE).a(port).reset());
        println(ansi().a("Service directory: ").fg(WHITE).a(
            (Boolean) opts.get("--memory") ? "MEMORY mode" :
                opts.get("--log-path")).reset());
    }
}