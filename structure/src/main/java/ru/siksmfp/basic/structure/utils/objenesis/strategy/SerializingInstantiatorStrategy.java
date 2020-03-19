/**
 * Copyright 2006-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.siksmfp.basic.structure.utils.objenesis.strategy;

import ru.siksmfp.basic.structure.utils.objenesis.ObjenesisException;
import ru.siksmfp.basic.structure.utils.objenesis.instantiator.ObjectInstantiator;
import ru.siksmfp.basic.structure.utils.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
import ru.siksmfp.basic.structure.utils.objenesis.instantiator.gcj.GCJSerializationInstantiator;
import ru.siksmfp.basic.structure.utils.objenesis.instantiator.perc.PercSerializationInstantiator;
import ru.siksmfp.basic.structure.utils.objenesis.instantiator.sun.SunReflectionFactorySerializationInstantiator;

import java.io.NotSerializableException;
import java.io.Serializable;

import static ru.siksmfp.basic.structure.utils.objenesis.strategy.PlatformDescription.GNU;
import static ru.siksmfp.basic.structure.utils.objenesis.strategy.PlatformDescription.HOTSPOT;
import static ru.siksmfp.basic.structure.utils.objenesis.strategy.PlatformDescription.JVM_NAME;
import static ru.siksmfp.basic.structure.utils.objenesis.strategy.PlatformDescription.OPENJDK;
import static ru.siksmfp.basic.structure.utils.objenesis.strategy.PlatformDescription.PERC;
import static ru.siksmfp.basic.structure.utils.objenesis.strategy.PlatformDescription.isGoogleAppEngine;

/**
 * Guess the best serializing instantiator for a given class. The returned instantiator will
 * instantiate classes like the genuine java serialization framework (the constructor of the first
 * not serializable class will be called). Currently, the selection doesn't depend on the class. It
 * relies on the
 * <ul>
 * <li>JVM version</li>
 * <li>JVM vendor</li>
 * <li>JVM vendor version</li>
 * </ul>
 * However, instantiators are stateful and so dedicated to their class.
 *
 * @author Henri Tremblay
 * @see ObjectInstantiator
 */
public class SerializingInstantiatorStrategy extends BaseInstantiatorStrategy {

    /**
     * Return an {@link ObjectInstantiator} allowing to create instance following the java
     * serialization framework specifications.
     *
     * @param type Class to instantiate
     * @return The ObjectInstantiator for the class
     */
    public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
        if (!Serializable.class.isAssignableFrom(type)) {
            throw new ObjenesisException(new NotSerializableException(type + " not serializable"));
        }
        if (JVM_NAME.startsWith(HOTSPOT) || PlatformDescription.isThisJVM(OPENJDK)) {
            // Java 7 GAE was under a security manager so we use a degraded system
            if (isGoogleAppEngine() && PlatformDescription.SPECIFICATION_VERSION.equals("1.7")) {
                return new ObjectInputStreamInstantiator<T>(type);
            }
            return new SunReflectionFactorySerializationInstantiator<T>(type);
        } else if (JVM_NAME.startsWith(GNU)) {
            return new GCJSerializationInstantiator<T>(type);
        } else if (JVM_NAME.startsWith(PERC)) {
            return new PercSerializationInstantiator<T>(type);
        }

        return new SunReflectionFactorySerializationInstantiator<T>(type);
    }

}
