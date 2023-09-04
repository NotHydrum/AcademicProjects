module Map (empty, singleton, insert, Map.null, size, member, delete, unionWith, fromList, Map.lookup) where

empty :: [(k,a)]
empty = []

singleton :: Ord k => k -> a -> [(k,a)]
singleton k a = [(k,a)]

insert :: Ord k => k -> a -> [(k,a)] -> [(k,a)]
insert k a [] = [(k,a)]
insert k a (x:ms)
    | k < fst x = (k,a):x:ms
    | k == fst x = (k,a):ms
    | otherwise = x:(insert k a ms)

null :: [(k,a)] -> Bool
null ms = Prelude.null ms

size :: [(k,a)] -> Int
size ms = length ms

member :: Ord k => k -> [(k,a)] -> Bool
member k ms = foldl (\b (x,_) -> b || x == k) False ms

delete :: Ord k => k -> [(k,a)] -> [(k,a)]
delete k ms = filter (\(x,_) -> x /= k) ms

unionWith :: Ord k => (a -> a -> a) -> [(k,a)] -> [(k,a)] -> [(k,a)]
unionWith _ [] ys = ys
unionWith _ xs [] = xs
unionWith f (x:xs) (y:ys)
    | fst x < fst y = x:(unionWith f xs (y:ys))
    | fst x > fst y = y:(unionWith f (x:xs) ys)
    | otherwise = ((fst x,f (snd x) (snd y))):(unionWith f xs ys)

fromList :: Ord k => [(k,a)] -> [(k,a)]
fromList xs = foldl (\ms (x,y) -> insert x y ms) [] xs

lookup :: Ord k => k -> [(k,a)] -> Maybe a
lookup k ms = Prelude.lookup k ms
