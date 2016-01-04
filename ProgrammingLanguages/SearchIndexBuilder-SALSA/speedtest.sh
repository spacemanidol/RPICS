export SALSAJAR=/Users/daniel/Desktop/salsa/salsa1.1.5.jar
export documents=200
java -cp $SALSAJAR:. pa3.Index collection/ outputdir/ 10 $documents;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 10 $documents 192.168.1.100 theaters.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 10 $documents 192.168.1.100 theaters1.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 10 $documents 192.168.1.100 theaters2.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 10 $documents 192.168.1.100 theaters3.txt;
echo "\n"
java -cp $SALSAJAR:. pa3.Index collection/ outputdir/ 5 $documents;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 5 $documents 192.168.1.100 theaters.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 5 $documents 192.168.1.100 theaters1.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 5 $documents 192.168.1.100 theaters2.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 5 $documents 192.168.1.100 theaters3.txt;
echo "\n"
java -cp $SALSAJAR:. pa3.Index collection/ outputdir/ 2 $documents;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 2 $documents 192.168.1.100 theaters.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 2 $documents 192.168.1.100 theaters1.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 2 $documents 192.168.1.100 theaters2.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 2 $documents 192.168.1.100 theaters3.txt;
echo "\n"
java -cp $SALSAJAR:. pa3.Index collection/ outputdir/ 1 $documents;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 1 $documents 192.168.1.100 theaters.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 1 $documents 192.168.1.100 theaters1.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 1 $documents 192.168.1.100 theaters2.txt;
java -cp $SALSAJAR:. pa3.DistributedIndex collection/ output/ 1 $documents 192.168.1.100 theaters3.txt;