#/bin/bash

for i in $(seq 13 27)
do
	#for j in $(seq 1 100)
#	do
	#echo $i $j
		qsub run_gehypdp_cluster.sh $i

#	done
done
