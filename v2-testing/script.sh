#/bin/bash

for i in $(seq 244412 244413 244414 244415 244417 244420 244421 244422)
do
   echo $i
done

var=$(grep "Seed: +" run_gehypdp_cluster.sh.o{244412..244413})

echo $var
