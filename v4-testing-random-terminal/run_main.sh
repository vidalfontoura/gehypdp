#/bin/bash

for i in $(seq 3 3)
do
	#for j in $(seq 1 100)
#	do
	#echo $i $j
		qsub run_gehypdp_cluster.sh $i

#	done
done
