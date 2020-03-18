package org.arquillian.cube.kubernetes.api;

import java.net.URL;
import java.util.Collection;

public interface KubernetesResourceLocator extends WithToImmutable<KubernetesResourceLocator> {

    /**
     * Locates the main kubernetes resource.
     *
     * @return Returns the url that points to the resource.
     */
    URL locate();

    /**
     * Locates the kubernetes resource from target which is generated by
     * embedded build of fabric8 maven plugin during test execution.
     *
     * @return Returns the url that points to the resource.
     */
    URL locateFromTargetDir();

    /**
     * Locate additional resources (such as ImageStreams) that
     * should be created in the test namespace.
     *
     * @return a collection of urls to additional resources.
     */
    Collection<URL> locateAdditionalResources();
}