/**
Copyright © 2019  Atos Spain SA, UAegean. All rights reserved.
This file is part of SEAL Configuration Manager (SEAL ConfMngr).
SEAL ConfMngr is free software: you can redistribute it and/or modify it under the terms of EUPL 1.2.
THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
See README file for the full disclaimer information and LICENSE file for full license information in the project root.
*/
package eu.seal.cm.params_api;

import java.io.IOException;
//import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;

//import com.nimbusds.jose.JWSAlgorithm;

/**
 *
 * @author UAegean
 */
public interface KeyStoreService {

	//public Key getJWEKey() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, UnsupportedEncodingException;
    public Key getHttpSigningKey() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException,UnsupportedEncodingException;
    //public Key getJWTPublicKey() throws KeyStoreException, UnsupportedEncodingException;
    public Key getHttpSigPublicKey() throws KeyStoreException, UnsupportedEncodingException;
    //public JWSAlgorithm getAlgorithm();
    public byte[] getX509PubKeytoRSABinaryFormat(Key key) throws IOException, KeyStoreException;
    public String getFingerPrintFromStringPubKey(String pubkey) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
