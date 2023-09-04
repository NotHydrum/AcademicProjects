import Cesar
import Vigenere
import Substitui
import Tests
import Library
import System.Environment
import System.Exit

main = do
    args <- getArgs
    valid <- return (validate args)
    case valid of
        False -> do
            putStrLn "Usage:"
            putStrLn "Main -t                    -- Runs tests"
            putStrLn "Main method direction key  -- Runs program which reads a message from stdin and outputs the encrypted/decrypted version to stdout."
            putStrLn "                              'method' should be replaced with the wanted method of encryption: 'cesar', 'vigenere' or 'substitui'."
            putStrLn "                              'direction' should be replaced with 'enc' if you want to encrypt a message or 'dec' if you want to decrypt a previously encrypted one."
            putStrLn "                              'key' should be replace with the key used for encryption/decryption. Should be an integer if using method 'cesar', otherwise a word in all capital letters should be used."
        otherwise -> do
            case (args !! 0) of
                "-t" -> do
                    runTests
                otherwise -> do
                    case (args !! 0) of
                         "cesar" -> executeInt (if args !! 1 == "enc" then cesarEncrypt else cesarDecrypt) (read (args !! 2))
                         "vigenere" -> executeString (if args !! 1 == "enc" then vigenereEncrypt else vigenereDecrypt) (args !! 2)
                         otherwise -> executeString (if args !! 1 == "enc" then substituiEncrypt else substituiDecrypt) (args !! 2)

validate :: [String] -> Bool
validate xs
    | length xs == 1 = xs !! 0 == "-t"
    | length xs == 3 = (elem (xs !! 0) ["cesar","vigenere","substitui"]) && (elem (xs !! 1) ["enc","dec"]) && if xs !! 0 == "cesar" then True else validateKey (xs !! 2)
    | otherwise = False

validateKey :: String -> Bool
validateKey [] = True
validateKey (x:xs) = elem x capitals && validateKey xs

executeInt :: (String -> Int -> String) -> Int -> IO ()
executeInt f c = do
    message <- getLine
    newMessage <- return (f message c)
    putStrLn newMessage
    executeInt f c
    return ()

executeString :: (String -> String -> String) -> String -> IO ()
executeString f c = do
    message <- getLine
    newMessage <- return (f message c)
    putStrLn newMessage
    executeString f c
    return ()

--Amar pelos dois
