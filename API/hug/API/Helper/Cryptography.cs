using Microsoft.IdentityModel.Tokens;
using System.Security.Cryptography;
using System.Text;

namespace API.Helper
{
    public static class Cryptography
    {
        private const string SecurityKey = "DecryptIfYouCan";
        public static string Encrypt(string value)
        {
            try
            {
                byte[] toEncryptedArray = UTF8Encoding.UTF8.GetBytes(value);

                MD5CryptoServiceProvider objMD5CryptoService = new MD5CryptoServiceProvider();
                byte[] securityKeyArray = objMD5CryptoService.ComputeHash(UTF8Encoding.UTF8.GetBytes(SecurityKey));
                objMD5CryptoService.Clear();

                var objTripleDESCryptoService = new TripleDESCryptoServiceProvider();
                objTripleDESCryptoService.Key = securityKeyArray;
                objTripleDESCryptoService.Mode = CipherMode.ECB;
                objTripleDESCryptoService.Padding = PaddingMode.PKCS7;

                var objCrytpoTransform = objTripleDESCryptoService.CreateEncryptor();
                byte[] resultArray = objCrytpoTransform.TransformFinalBlock(toEncryptedArray, 0, toEncryptedArray.Length);
                objTripleDESCryptoService.Clear();
                return Convert.ToBase64String(resultArray, 0, resultArray.Length);
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message, ex.InnerException);
            }
        }

        public static string Decrypt(string value)
        {
            try
            {
                byte[] toEncryptArray = Convert.FromBase64String(value);
                MD5CryptoServiceProvider objMD5CryptoService = new MD5CryptoServiceProvider();

                byte[] securityKeyArray = objMD5CryptoService.ComputeHash(UTF8Encoding.UTF8.GetBytes(SecurityKey));
                objMD5CryptoService.Clear();

                var objTripleDESCryptoService = new TripleDESCryptoServiceProvider();
                objTripleDESCryptoService.Key = securityKeyArray;
                objTripleDESCryptoService.Mode = CipherMode.ECB;
                objTripleDESCryptoService.Padding = PaddingMode.PKCS7;

                var objCrytpoTransform = objTripleDESCryptoService.CreateDecryptor();
                byte[] resultArray = objCrytpoTransform.TransformFinalBlock(toEncryptArray, 0, toEncryptArray.Length);
                objTripleDESCryptoService.Clear();

                return UTF8Encoding.UTF8.GetString(resultArray);
            }
            catch (Exception ae)
            {
                throw new Exception(ae.Message, ae.InnerException);
            }
        }
    }
}
