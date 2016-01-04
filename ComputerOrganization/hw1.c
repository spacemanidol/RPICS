/*********************************************************************************/
/* Assignment 1: 2-D/ NxN Matrix Swap and  Multiplication ************************/
/* Name: Daniel Campos **************************************************/
/*********************************************************************************/
/*********************************************************************************/
/* Usual suspects to include *****************************************************/
/*********************************************************************************/
#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>
#include<errno.h>
/*********************************************************************************/
/* Function Calls ****************************************************************/
/*********************************************************************************/
double **mm_alloc( int size )
{
    /*
     * hints: allocate an array of pointers to a double,
     * then allocate an row/array of doubles and assign each double pointer that row
     * use calloc for allocation and zeroing out all the bytes
     */
    //implementation based upon suggestion above 
    double **matrix;
    int i;
    matrix=(double** )calloc(size, sizeof(double*));//allocates the array of pointers
    for( i=0;i< size;i++){
        matrix[i]=(double*)calloc(size, sizeof(double));// then allocates a row/array of double and assign each double pointers in that row
    }
    return( matrix );//returns it
}
void mm_free( int size, double **matrix )
{
    /*
     * hint: reverse of the allocation process
     * by freeing each array of doubles,
     * then free the array of double pointers
     */
    //implementation based upon suggestion above
    int i;
    for( i=0;i<size;i++)
    {
        free(matrix[i]);//first it frees each array of doubles
    }
    free(matrix);//then frees the array of doubles
}

double **mm_read( char *filename, int *size )
{
    /*
     * hints: fopen the file, then use fgets to read each line of data.
     * The first line is the size of the matrix, so read it first.
     * make sure you record the "size" in the size argument.
     * Next alloc each of the matrixes.
     * Use sscanf to take the line of text and parse out the matrix entry.
     */
    int i=0;int j=0;
    double temp;
    char format[]="%s";//to allow to take in strings as usable data
    double **matrix = NULL;
    char bufferfilename[sizeof(format)+100];// a buffer to allow up to 100 character long strings for the file name
    sprintf(bufferfilename,format,filename);
    FILE * inFile;
    inFile=fopen(bufferfilename,"r");
    char buffer[100];// a buffer for the line being read in
    fgets(buffer,10,inFile);
    sscanf(buffer, "%i", size);//gets the size of the matrix in the top value
    matrix =mm_alloc(*size);// creates a matrix of the proper size
    while(fgets(buffer,40,inFile)!=NULL)//gets the values
    {
        sscanf(buffer, "%lf",&temp);// takes in the information and saves it in temp
        matrix[i][j]=temp;//sets the matrix spot equal to
        j++;// moves down the line
        if(j==*size)//when it finaly is down the end of the line it resets and then moves down a row
        {
            j=0;
            i++;
        }
        if(i==*size)// as soon as the size is reached therefore filled it breaks out
            break;
    }
    fclose(inFile);//closes file to prevent memory leak
    return matrix;//read in based upon reading online and previous documents created
}
void mm_print( int size, double **matrix )
{
    /*
     * iterate over the matrix entries across each row.
     * when a row is complete insert a newline character "\n"
     */
    FILE *outFile;
    outFile =fopen("test_matrix.dat","w");//opens the file
    int i,j=0;
    fprintf(outFile,"%s", "Output of Result Matrix\n");//prints out begining
    fprintf(outFile,"%s","/********** Start of 2D Matrix *********\n");//designates begining of the matrix
    for(i=0;i<size;i++)//loops through the colums
    {
        for(j=0;j<size;j++)
        {//loops through the rows
            fprintf(outFile, "%f\t\t",matrix[i][j]);//prints em out
        }
        fprintf(outFile,"  \n");//a new line
        
    }
    fprintf(outFile, "%s", "********** End of matrix ***********\n");//designates end of the matrix
    fclose(outFile);//closes file to prevent a memory leak
    //based on previous usage of c and c++
}
double **mm_swap( int size, double **matrix )
{
    /*
     * hints: allocate space for swapped matrix
     * compute swap matrix using original matrix as input.
     */
    
    double **swap=NULL;
    swap =mm_alloc(size);//creates the new one with the correct size
    int i=0;
    int j=0;
    for(i=0; i<size-2;i++)//does the shift and ends two before the end of it so that it isnt trying to shift with areas that do actually have stuff
    {
        for(j=0; j<size;j++)//moves the first colum over two spaces to the right
        {
            swap[j][i]=matrix[j][i+2];
        }
        for(j=0;j<size;j++)//moves the colum on the right to the left
        {
            swap [j][i+2]=matrix[j][i];
        }
    }
    return( swap ); // return the swap matrix
    //based on hint and time
}

double **mm_matrix_mult( int size, double **matrix, double **swap )
{
    /*
     * hints: alloc a results matrix,
     * do the matrix multiple per the quad loop method.
     * this is an extension of regular 2-D matrix mult.
     * return the results_matrix
     */
    double ** result =NULL;
    result =mm_alloc(size);//create a memmory allocation of proper size for the results
    int i=0; int j=0; int k=0;
    double temp;//temp var to save stuff
    double temp2=0;//second temp var for the results
    for(i=0;i<size;i++)
    {
        for(j=0;j<size;j++)//for the triple region
        {
            temp=0;
            for(k=0;k<size;k++)//checks it
            { 
                temp2=matrix[i][k]*swap[k][j];//gets the multiplication
                temp+=temp2;//then adds it to the temp so that the matrix thing works
            }
            result[i][j]=temp;// set the new result as the added temps
        }
    }
    return result;//based upon the matrix mutiplication wikipedia page secion 4.2 with what is required to do a matrix multiplication of 2 cubes
}
int main()
{
    /*
     * you must keep this function AS IS.
     * you cannot modify it!
     */
    char filename[256];
    double **matrix=NULL;
    double **swap_matrix=NULL;
    double **results_matrix=NULL;
    int size=0;
    
    printf("Please enter the matrix data file name: ");
    scanf( "%s", filename );
    
    matrix = mm_read( filename, &size );
    swap_matrix = mm_swap( size, matrix );
    results_matrix = mm_matrix_mult( size, matrix, swap_matrix );
    
    mm_print( size, results_matrix );
    mm_free( size, matrix );
    mm_free( size, swap_matrix );
    mm_free( size, results_matrix );
    return( 0 );
}
