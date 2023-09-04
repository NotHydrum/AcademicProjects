main = do
    x <- getLine
    y <- readNumbers 0 (read x)
    print y

readNumbers :: Int -> Int -> IO(Int)
readNumbers x 0 = return x
readNumbers x y = do
    number <- getLine
    readNumbers (read number + x) (y - 1)


