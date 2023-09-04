module Expressoes (Expr, constante, variavel, (|+|), (|*|), avalia) where

data Expr = Constant Int | Variable String | Sum Expr Expr | Product Expr Expr
instance Eq Expr where
    x == y = avalia [] x == avalia [] y
instance Show Expr where
    show (Constant a) = show a
    show (Variable a) = a
    show (Sum x y) = "(" ++ show x ++ " + " ++ show y ++ ")"
    show (Product x y) = "(" ++ show x ++ " * " ++ show y ++ ")"

constante :: Int -> Expr
constante a = Constant a

variavel :: String -> Expr
variavel a = Variable a

(|+|):: Expr -> Expr -> Expr
(|+|) x y = Sum x y

(|*|):: Expr -> Expr -> Expr
(|*|) x y = Product x y

avalia :: [(String,Int)] -> Expr -> Int
avalia _ (Constant a) = a
avalia xs (Variable a) = maybeInt (lookup a xs)
avalia xs (Sum y z) = avalia xs y + avalia xs z
avalia xs (Product y z) = avalia xs y * avalia xs z

maybeInt :: Maybe Int -> Int
maybeInt (Just a) = a
maybeInt Nothing = 0
