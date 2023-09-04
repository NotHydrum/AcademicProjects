import Control.Monad
import Test.QuickCheck

--Chapter 11
--Exercise 1
--(a)
prop_integer_oddAfterEven :: Int -> Property
prop_integer_oddAfterEven x = even x ==> odd (x + 1)

--(b)
isPrime :: Int -> Bool
isPrime n = n > 1 && not (any isFactor [2..n-1])
    where isFactor m = n `mod` m == 0

mersenne :: Int -> Int
mersenne x = 2 ^ x - 1

mersennePrimeTest :: Int -> Property
mersennePrimeTest x = isPrime x ==> isPrime (mersenne x)

--Exercise 4
data Shape = Circle Double --radius
           | Rect Double Double --length, height
           | Triang Double Double Double --sides
    deriving Show

--(a)
instance Arbitrary Shape where
    arbitrary = do
        n <- choose (1, 3) :: Gen Int
        case n of
            1 -> do a <- choose (1.0,100.0)
                    return $ Circle a
            2 -> do a <- choose (1.0,100.0)
                    b <- choose (1.0,100.0)
                    return $ Rect a b
            3 -> do a <- choose (1.0,100.0)
                    b <- choose (1.0,a)
                    c <- choose (a-b+1,a+b-1)
                    return $ Triang a b c

--(c)
perimeter :: Shape -> Double
perimeter (Circle a) = 2 * pi * a
perimeter (Rect a b) = 2 * a + 2 * b
perimeter (Triang a b c) = a + b + c

prop_shape_perimeter :: Shape -> Bool
prop_shape_perimeter x = perimeter x > 0

--(d)
isTriangle :: Shape -> Bool
isTriangle (Triang _ _ _) = True
isTriangle _ = False

triangleInequality :: Shape -> Bool
triangleInequality (Triang a b c) = a + b > c && a + c > b && b + c > a

prop_shape_triangle :: Shape -> Property
prop_shape_triangle x = isTriangle x ==> triangleInequality x

--Exercise 6
data Tree a = EmptyTree | Node (Tree a) a (Tree a) deriving Show

--(a)
instance Arbitrary a => Arbitrary (Tree a) where
    arbitrary = oneof [return EmptyTree, liftM3 Node arbitrary arbitrary arbitrary]

--(e)
size :: Tree a -> Int
size EmptyTree = 0
size (Node lf _ rg) = 1 + size lf + size rg

flatten :: Tree a -> [a]
flatten EmptyTree = []
flatten (Node lf a rg) = a : (flatten lf ++ flatten rg)

prop_flatten_size :: Tree a -> Bool
prop_flatten_size x = length (flatten x) == size x

--(f)
invert :: Tree a -> Tree a
invert EmptyTree = EmptyTree
invert (Node lf a rg) = Node (invert rg) a (invert lf)

prop_invert_size :: Tree a -> Bool
prop_invert_size x = size x == size (invert x)

--(g)
makeTree :: [a] -> Tree a
makeTree [] = EmptyTree
makeTree (x:xs) = Node lf x rg where
    half = length xs `div`2
    lf = makeTree $ take half xs
    rg = makeTree $ drop half xs

prop_makeTree_size :: Eq a => [a] -> Bool
prop_makeTree_size xs = xs == flatten (makeTree xs)
