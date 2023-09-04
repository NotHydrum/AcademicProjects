votosCandidato :: [(String, (String, String, String))] -> String -> Int
votosCandidato [] _ = 0
votosCandidato (x:xs) y = contar (snd x) y + votosCandidato xs y

contar :: (String, String, String) -> String -> Int
contar (w, x, z) y
    | y == w = 3
    | y == x = 2
    | y == z = 1
    | otherwise = 0

vencedorEleicao :: [(String, (String, String, String))] -> String
vencedorEleicao xs = fst (determinarVencedor (mapearPontos xs))

mapearPontos:: [(String, (String, String, String))] -> [(String, Int)]
mapearPontos xs = zip listaCandidatos [votosCandidato xs y | y <- listaCandidatos]
    where listaCandidatos = [fst x | x <- xs]

determinarVencedor :: [(String, Int)] -> (String, Int)
determinarVencedor (x:xs) = foldl (\x y -> if snd y > snd x then y else x) x xs
