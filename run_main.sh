#/bin/bash

for i in $(seq 1 30)
do
	#for j in $(seq 1 100)
#	do
	#echo $i $j
		qsub run_gehypdp_cluster.sh $1

#	done
done
