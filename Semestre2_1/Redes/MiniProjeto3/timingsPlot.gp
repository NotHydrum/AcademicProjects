set terminal svg size 350,262
set xrange [0:2100]
set yrange [0:10000]
set logscale y 10
set output 'plot.svg'
plot 'A_SYNFlood_#1.txt'
