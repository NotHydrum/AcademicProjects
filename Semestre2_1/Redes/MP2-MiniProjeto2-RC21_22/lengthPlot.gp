# MP2 - Group 27
set terminal svg size 350,262
set output 'plot.svg'
set xrange [0:1504]
set yrange [0:100]
set style line 1 \
    linecolor rgb '#FFA500' \
    linetype 1 linewidth 2 \
    pointtype 7 pointsize 0.5
set key noautotitle
set title "y - Percentage of packets with length bigger than x"
plot 'GraphData.txt' with linespoints linestyle 1
