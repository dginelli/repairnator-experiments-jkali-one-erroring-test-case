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
package ru.siksmfp.basic.structure.utils.objenesis.instantiator.util;

import ru.siksmfp.basic.structure.utils.objenesis.ObjenesisException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Helper class basically allowing to get access to {@code sun.misc.Unsafe}
 *
 * @author Henri Tremblay
 */
public final class UnsafeUtils {

    private static final Unsafe unsafe;

    static {
        Field f;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
        } catch (NoSuchFieldException e) {
            throw new ObjenesisException(e);
        }
        f.setAccessible(true);
        try {
            unsafe = (Unsafe) f.get(null);
        } catch (IllegalAccessException e) {
            throw new ObjenesisException(e);
        }
    }

    private UnsafeUtils() {
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }
}
