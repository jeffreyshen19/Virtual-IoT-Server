import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLClientSocket {

 private String mHost;
 private int mPort;
    private char[] mCacertsPassword = "changeit".toCharArray();
    private SSLSocket mSSLSocket = null;
    private final boolean DEBUG_ALL = false;

 public SSLClientSocket(String host, int port) {
  super();
  mHost = host;
  mPort = port;
 }

 public boolean checkAndAddCertificates() {
  KeyStore keyStore = getKeyStore();
  CertificateTrustManager certificateTrustManager = getCertificateTrustManager(keyStore);
  mSSLSocket = getSSLSocket(certificateTrustManager);
        if(testSSLSocket(mSSLSocket)) {
         return true;
        }
        else {
         closeSSLSocket();
         return addCertificates(certificateTrustManager.getServerX509Certificates(), keyStore);
        }
 }

 public SSLSocket getSSLSocket() {
  if(mSSLSocket == null) {
   KeyStore keyStore = getKeyStore();
   CertificateTrustManager certificateTrustManager = getCertificateTrustManager(keyStore);
   mSSLSocket = getSSLSocket(certificateTrustManager);
  }
  return mSSLSocket;
 }

 private void closeSSLSocket() {
        try {
   mSSLSocket.close();
   mSSLSocket = null;
  } catch (IOException e) {
   e.printStackTrace();
  }
 }

 private boolean addCertificates(X509Certificate[] x509Certificates, KeyStore keyStore) {
  if(x509Certificates == null || x509Certificates.length == 0) {
         if(DEBUG_ALL) System.out.println("Error, there is no any certificate");
   return false;
  }
  try {
         for (int i = 0; i < x509Certificates.length; ++i) {
    X509Certificate cert = x509Certificates[i];
    String alias = mHost + "-" + i;
    keyStore.setCertificateEntry(alias, cert);
    if(DEBUG_ALL) System.out.println(alias + " is added");
         }
   OutputStream out = new FileOutputStream("jssecacerts");
   keyStore.store(out, mCacertsPassword);
         out.flush();
         out.close();
         return true;
  } catch (Exception e) {
   e.printStackTrace();
   return false;
  }
 }

 private boolean testSSLSocket(SSLSocket sslSocket) {
        try {
         if(DEBUG_ALL) System.out.println("Starting SSL handshake");
      sslSocket.setSoTimeout(0);
            sslSocket.startHandshake();
            return true;
        } catch (Exception e) {
         if(DEBUG_ALL) e.printStackTrace();
            return false;
        }
 }

 private SSLSocket getSSLSocket(CertificateTrustManager certificateTrustManager) {
  SSLSocket sslSocket = null;
  try {
   SSLContext context = SSLContext.getInstance("TLS");
         context.init(null, new TrustManager[]{certificateTrustManager}, null);
         SSLSocketFactory factory = context.getSocketFactory();
         if(DEBUG_ALL) System.out.println("Opening connection to " + mHost + ":" + mPort);
         sslSocket = (SSLSocket) factory.createSocket(mHost, mPort);
  } catch (Exception e) {
   e.printStackTrace();
  }
  return sslSocket;
 }

 private CertificateTrustManager getCertificateTrustManager(KeyStore keyStore) {
  CertificateTrustManager certificateTrustManager = null;
  try {
   TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
   tmf.init(keyStore);
         X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
         certificateTrustManager = new CertificateTrustManager(defaultTrustManager);
  } catch (Exception e) {
   e.printStackTrace();
  }
  return certificateTrustManager;
 }

 private KeyStore getKeyStore() {
        File file = new File("jssecacerts");
        if (file.isFile() == false) {
            char SEP = File.separatorChar;
            File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
            file = new File(dir, "jssecacerts");
            if (file.isFile() == false) {
                file = new File(dir, "cacerts");
            }
        }
        if(DEBUG_ALL) System.out.println("Loading KeyStore " + file);
        KeyStore ks = null;
  try {
         InputStream in = new FileInputStream(file);
   ks = KeyStore.getInstance(KeyStore.getDefaultType());
         ks.load(in, mCacertsPassword);
         in.close();
  } catch (Exception e) {
   e.printStackTrace();
  }
        return ks;
 }

    private static class CertificateTrustManager implements X509TrustManager {

        private final X509TrustManager mX509TrustManager;
        private X509Certificate[] mServerX509Certificates;

        CertificateTrustManager(X509TrustManager x509TrustManager) {
            mX509TrustManager = x509TrustManager;
        }

  @Override
  public void checkClientTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {

  }

  @Override
  public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
   mServerX509Certificates = x509Certificates;
   mX509TrustManager.checkServerTrusted(x509Certificates, authType);
  }

  @Override
  public X509Certificate[] getAcceptedIssuers() {
   return null;
  }

  public X509Certificate[] getServerX509Certificates() {
   return mServerX509Certificates;
  }
    }

}
