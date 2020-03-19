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
package org.takes.rq.multipart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.takes.Request;
import org.takes.misc.Utf8String;

/**
 * Test case for {@link RqTemp}.
 * @author Nicolas Filotto (nicolas.filotto@gmail.com)
 * @version $Id$
 * @since 0.33
 */
public final class RqTempTest {

    /**
     * RqTemp can delete the underlying temporary file.
     * @throws IOException if some problem occurs.
     */
    @Test
    public void deletesTempFile() throws IOException {
        final File file = File.createTempFile(
            RqTempTest.class.getName(),
            ".tmp"
        );
        Files.write(
            file.toPath(),
            new Utf8String("Temp file deletion test").bytes()
        );
        final Request request = new RqTemp(file);
        try {
            MatcherAssert.assertThat(
                "File is not created!",
                file.exists(),
                Matchers.is(true)
            );
        } finally {
            request.body().close();
        }
        MatcherAssert.assertThat(
            "File exists after stream closure",
            file.exists(),
            Matchers.is(false)
        );
    }
}
