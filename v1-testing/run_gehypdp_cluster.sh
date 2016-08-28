#/bin/bash
#########################################
## Parametros que le pasamos al script ##
#########################################
#$ -S /bin/bash
#######################################
# Usar el directorio de trabajo actual
#######################################
#$ -cwd
# Tiempo de trabajo
#$ -l h_rt=2400:00:00
# juntar la salida estandar y de error en un solo fichero
#$ -j y
###########################
# usar colas indicadas
###########################
#$ -q loki.q
# #$ -q 2014all.q
##$ -t 1-30:1
##$ -o /dev/null



######################## Run the algorithm ################
# echo ""
echo "Host --> $HOSTNAME"

echo Init Time: `date`
init=`date +'%s'`

echo "Seed: "+$1
#echo "Instance: "+$2
#echo "Time: "+$3

java -Xmx3000M -Xms3000M -jar gehypdp-jar-with-dependencies.jar $1



final=`date +'%s'`
echo End  Time: `date` --- run in $(( (($final-$init)/3600) )):$(( (($final-$init)/60)%60 )):$(( (($final-$init))%60 )) ---

######################## end of running the algorithm ################

