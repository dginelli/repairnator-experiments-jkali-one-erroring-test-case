/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.tk;

import java.io.InputStream;
import java.net.URL;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsHtml;

/**
 * HTML take.
 *
 * <p>This take returns an HTML response by wrapping the provided
 * content into {@link org.takes.rs.RsHtml}.
 *
 * <p>The class is immutable and thread-safe.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class TkHtml extends TkWrap {

    /**
     * Ctor.
     * @param body Text
     */
    public TkHtml(final String body) {
        super(
            new Take() {
                @Override
                public Response act(final Request req) {
                    return new RsHtml(body);
                }
            }
        );
    }

    /**
     * Ctor.
     * @param body Body with HTML
     */
    public TkHtml(final byte[] body) {
        super(
            new Take() {
                @Override
                public Response act(final Request req) {
                    return new RsHtml(body);
                }
            }
        );
    }

    /**
     * Ctor.
     * @param url URL with content
     */
    public TkHtml(final URL url) {
        super(
            new Take() {
                @Override
                public Response act(final Request req) {
                    return new RsHtml(url);
                }
            }
        );
    }

    /**
     * Ctor.
     * @param body Content
     */
    public TkHtml(final InputStream body) {
        super(
            new Take() {
                @Override
                public Response act(final Request req) {
                    return new RsHtml(body);
                }
            }
        );
    }

}
