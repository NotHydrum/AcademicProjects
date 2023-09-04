import Data.Char

--Chapter 1
--Exercise 1
addThreeNumbers x y z = x + y + z
addThreePositiveNumbers x y z = if x > 0 && y > 0 && z > 0 then x + y + z else 0

--Exercise 2
distance x y z = abs (y - x) < z

--Exercise 3
addDigit x y = x * 10 + (if x < 0 then (-y) else y)

--Exercise 4
--(a) - ['a','b'] - 2 elements
--(b) - "ab" - 2 elements
--(c) - [['a','b']] - 1 element
--(d) - [['a','b'], ['c','d']] - 2 elements
--(e) - [[['a','b']]] - 1 element
--(f) - [] - 0 elements
--(g) - [[]] - 1 element
--(h) - [[], []] - 2 elements

--Exercise 5
--(a)
longerThan10 xs = length xs > 10
--(b)
notEmpty xs = not (null xs)
--(c)
deleteFirstLastChars xs = tail (init xs)
--(d)
secondChar xs = head (tail xs)
--(e)
penultimateChar xs = last (init xs)
--(f)
nthChar n xs = last (take (n + 1) xs)
--(g)
reverseTail xs = head xs : reverse (tail xs)
--(h)
sumFirstFive xs = sum (take 5 xs)
--(i)
sumFirstN n xs = sum (take n xs)
--(j)
lastElementEqual xs ys = not (null xs) && not (null ys) && length xs == length ys && last xs == last ys

--Exercise 8
--(a) - [2,4,6]
--(b) - [4,16,36,64]
--(c) - "6789"
--(d) - [(1,1),(1,2),(1,3),(3,1),(3,2),(3,3)]
--(e) - [(1,1),(1,2),(1,3),(3,1),(3,2),(3,3)]

--Exercise 9
sumHundredSquares = sum [x ^ 2 | x <- [1..100]]

--Exercise 10
pitagoras n = [(x,y,z) | x <- [1..n], y <- [1..n], x <= y, z <- [1..n], x ^ 2 + y ^ 2 == z ^ 2]

--Exercise 11
--(a)
factors n = [x | x <- [1..n], mod n x == 0]
--(b)
perfect n = [x | x <- [1..n], sum (factors x) - x == x]

--Exercise 12
powersOf2 = [2 ^ x | x <- [0..]]

--Exercise 13
dotProduct xs ys = sum [nthChar n xs * nthChar n ys | n <- [0..length xs - 1]]

--Exercise 14
reproduct xs = concat [replicate x x | x <- xs]

--Exercise 15
generate = concat [[(x,y) | y <- [4,5,6]] | x <- [1,2,3]]

--Exercise 17
pairs n = [(x,y) | x <- [1..n], y <- [1..n], x /= y]

--Chapter 2
--Exercise 1

--Exercise 2
--a, b, d, e, f, g, h

--Chapter 3
--Exercise 1
--(a)
firstElementDouble (x,_) = x
--(b)
swapElements (x,y) = (y,x)
--(c)
firstElementTriple (x,_,_) = x
--(d)
swapElementsTriple (x,y,z) = (y,x,z)
--(e)
secondElementList (_:x:_) = x
--(f)
secondElementFirstDouble ((_,x):_) = x

--Exercise 5
complexQuadrant (x,y)
    | x > 0 && y > 0 = "1st Quadrant"
    | x < 0 && y > 0 = "2nd Quadrant"
    | x < 0 && y < 0 = "3rd Quadrant"
    | x > 0 && y < 0 = "4th Quadrant"
    | otherwise = "No Quadrant"

--Exercise 7
leetSpeak xs = [leetTransform x | x <- xs]
leetTransform x
    | x == 'a' || x == 'A' = '4'
    | x == 'i' || x == 'I' = '1'
    | x == 't' || x == 'T' = '7'
    | x == 'o' || x == 'O' = '0'
    | x == 's' || x == 'S' = '5'
    | x == 'e' || x == 'E' = '3'
    | otherwise = toUpper x

--Exercise 8
--(a)
safeTailCondition xs = if null xs then xs else tail xs
--(b)
safeTailGuard xs
    | null xs = xs
    | otherwise = tail xs
--(c)
safeTailPattern [] = []
safeTailPattern (x:xs) = xs

--Exercise 9
--(a)
halveWhere xs = (ys, zs)
    where ys = take (div (length xs) 2) xs
          zs = drop (div (length xs) 2) xs
--(b)
halveLetIn xs = let ys = take (div (length xs) 2) xs
                    zs = drop (div (length xs) 2) xs
                in (ys, zs)

--Chapter 4
--Exercise 1
--(a)
sum' [] = 0
sum' (x:xs) = x + sum' xs
--(c)
maximum' (x:[]) = x
maximum' (x:xs) = max x (maximum' xs)
--(e)
substitute x y [] = []
substitute y z (x:xs)
    | x == y = z:substitute y z xs
    | otherwise = x:substitute y z xs

--Exercise 2
binaryRep x
    | x < 2 = x
    | otherwise = binaryRep (div x 2) * 10 + mod x 2

--Chapter 5
--Exercise 4
--(a)
zipWithRecursive :: (a -> b -> c) -> [a] -> [b] -> [c]
zipWithRecursive _ _ [] = []
zipWithRecursive _ [] _ = []
zipWithRecursive f (x:xs) (y:ys) = (f x y):(zipWithRecursive f xs ys)
--(b)
zipWithList :: (a -> b -> c) -> [a] -> [b] -> [c]
zipWithList f xs ys = [f x y | (x, y) <- zip xs ys]
--(c)
zip' :: [a] -> [b] -> [(a, b)]
zip' xs ys = zipWith (,) xs ys

--Exercise 7
dropUntil :: (a -> Bool) -> [a] -> [a]
dropUntil f xs
    | f (head xs) = xs
    | otherwise = dropUntil f (tail xs)

--Exercise 8
--(c)
total :: (Int -> Int) -> Int -> Int
total f x = sum (map f [0..x])

--Exercise 9
--(a)
apply :: [a -> a] -> [a] -> [a]
apply _ [] = []
apply fs (x:xs) = (applySec fs x):(apply fs xs)

applySec :: [a -> a] -> a -> a
applySec [] x = x
applySec (f:fs) x = applySec fs (f x)

--Exercise 11
mult = \x y z -> x * y * z

--Exercise 13
isNonBlank :: Char -> Bool
isNonBlank = \x -> notElem x [' ','\n','\t']

--Exercise 17
sum'2 xs = foldl (+) 0 xs

length' xs = foldr (\_ y -> y + 1) 0 xs

--Exercise 18
map' f xs = foldr (\x ys -> (f x):ys) [] xs

filter' f xs = foldr (\x ys -> if f x then (x:ys) else ys) [] xs

--Exercise 20
indexOf :: Eq a => [a] -> a -> Int
indexOf xs x = foldr (\(y, z) w -> if y == x then z else w) (-1) (zip xs [0..])

--Exercise 21
poly :: Int -> [Int] -> Int
poly x xs = foldr (+) 0 [x * y | (x,y) <- zip xs (reverse [x ^ y | y <- [0..length xs - 1]])]

--Chapter 8
--Exercise 1
escrevePrimos :: [Int] -> IO ()
escrevePrimos xs = putStr (primosString xs)

primosString :: [Int] -> String
primosString [] = ""
primosString (x:xs) = show x ++ "º primo é " ++ show (primos!!x) ++ "\n" ++ primosString xs

primos :: [Int]
primos = sieve [2..]
    where sieve (p:xs) = p : sieve [x | x <- xs, x `mod`p > 0]

--Exercise 2
--(a)
printParity :: Int -> IO()
printParity x = putStrLn (if even x then "Par" else "Ímpar")
--(b)
showParity :: [Int] -> IO()
showParity [] = return ()
showParity (x:xs) = do
    printParity x
    showParity xs

--Exercise 5
guess :: Int -> IO()
guess x = do
    guesses <- guesser 0 x
    putStrLn ("Successful after " ++ show guesses ++ " tries!")

guesser :: Int -> Int -> IO(Int)
guesser lower upper = do
    let guess = div (lower + upper) 2
    putStr (show guess ++ "? ")
    response <- getLine
    case response of
         "<" -> do
                x <- guesser lower guess
                return (x + 1)
         ">" -> do
                x <- guesser guess upper
                return (x + 1)
         "=" -> return 1
         otherwise -> guesser lower upper

--Chapter 9
--Exercise 1
toFile :: Show a => FilePath -> [a] -> IO ()
toFile fp xs = do
    let string = fileString xs
    writeFile fp string

fileString :: Show a => [a] -> String
fileString [] = ""
fileString (x:xs) = (show x) ++ "\n" ++ (fileString xs)
