main = do
    x <- getLine
    putStrLn (if isPalindrome x
                then "Sim"
                else "Não")

isPalindrome:: String -> Bool
isPalindrome x = x == reverse x
