/**
 * Copyright 2013 Bruno Oliveira, and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.abstractj.kalium.crypto;

import org.abstractj.kalium.encoders.Hex;
import org.junit.Test;

import java.util.Arrays;

import static org.abstractj.kalium.fixture.TestVectors.BOX_CIPHERTEXT;
import static org.abstractj.kalium.fixture.TestVectors.BOX_MESSAGE;
import static org.abstractj.kalium.fixture.TestVectors.BOX_NONCE;
import static org.abstractj.kalium.fixture.TestVectors.SECRET_KEY;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SecretBoxTest {

    @Test
    public void testAcceptStrings() throws Exception {
        try {
            new SecretBox(SECRET_KEY);
        } catch (Exception e) {
            fail("SecretBox should accept strings");
        }
    }

    @Test(expected = RuntimeException.class)
    public void testNullKey() throws Exception {
        String key = null;
        new SecretBox(key);
        fail("Should raise an exception");
    }

    @Test(expected = RuntimeException.class)
    public void testShortKey() throws Exception {
        String key = "hello";
        new SecretBox(key);
        fail("Should raise an exception");
    }

    @Test
    public void testEncrypt() throws Exception {
        SecretBox box = new SecretBox(SECRET_KEY);

        byte[] nonce = Hex.decodeHexString(BOX_NONCE);
        byte[] message = Hex.decodeHexString(BOX_MESSAGE);
        byte[] ciphertext = Hex.decodeHexString(BOX_CIPHERTEXT);

        byte[] result = box.encrypt(nonce, message);
        assertTrue("failed to generate ciphertext", Arrays.equals(result, ciphertext));
    }

    @Test
    public void testDecrypt() throws Exception {

        SecretBox box = new SecretBox(SECRET_KEY);

        byte[] nonce = Hex.decodeHexString(BOX_NONCE);
        byte[] expectedMessage = Hex.decodeHexString(BOX_MESSAGE);
        byte[] ciphertext = box.encrypt(nonce, expectedMessage);

        byte[] message = box.decrypt(nonce, ciphertext);

        assertTrue("failed to decrypt ciphertext", Arrays.equals(message, expectedMessage));
    }

    @Test(expected = RuntimeException.class)
    public void testDecryptCorruptedCipherText() throws Exception {
        SecretBox box = new SecretBox(SECRET_KEY);
        byte[] nonce = Hex.decodeHexString(BOX_NONCE);
        byte[] message = Hex.decodeHexString(BOX_MESSAGE);
        byte[] ciphertext = box.encrypt(nonce, message);
        ciphertext[23] = ' ';

        box.decrypt(nonce, ciphertext);
        fail("Should raise an exception");
    }
}
