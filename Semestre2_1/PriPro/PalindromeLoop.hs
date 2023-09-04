main = do
    x <- getLine
    if null x
        then return ()
        else do
            putStrLn (if isPalindrome x
                        then "Sim"
                        else "Não")
            main

isPalindrome:: String -> Bool
isPalindrome x = x == reverse x
