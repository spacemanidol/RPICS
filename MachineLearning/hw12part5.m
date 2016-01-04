clear;
load digits.csv
st=310;
nd=650;
i = digits(:,2);
s = digits(:,3);
class = digits(:,1);
n=size(digits,1);
intensity=((i-min(i))/(max(i)-min(i)))*2-1;
symmetry=((s-max(s))/(max(s)-min(s)))*2+1;
for i=1:n
	if class(i)==1
		class(i)=1;
	else
		class(i)=-1;
	end
end
X=[intensity(st:nd) symmetry(st:nd)];
Y=class(st:nd);
inputs = X';
targets = Y';
Xtest=[intensity(nd+1:end) symmetry(nd+1:end)]';
Ytest=class(nd+1:end)';
C=0.99;
cvFolds = crossvalind('Kfold', Y, 10);
for i = 1:10                                  %# for each fold
    testIdx = (cvFolds == i);                %# get indices of test instances
    trainIdx = ~testIdx;                     %# get indices training instances

    %# train an SVM model over training instances
    svmModel = svmtrain(X(trainIdx,:), Y(trainIdx), 'Kernel_Function','polynomial','polyorder',5,'BoxConstraint',C);
    %# test using test instances
    pred = svmclassify(svmModel, Y(testIdx,:), 'Showplot',false);

    %# evaluate and update performance object
    cp = classperf(cp, pred, testIdx);
end

%# get accuracy
cp.CorrectRate

%# get confusion matrix
%# columns:actual, rows:predicted, last-row: unclassified instances
cp.CountingMatrix
cp = classperf(groups);   
mdl = svmtrain(X,Y,'Kernel_Function','polynomial','polyorder',5,'BoxConstraint',C);
figure;hold on;
X = [-1:.01:1];
Y = [-1:.01:1];
for i=1:size(X,2)
    for j=1:size(Y,2)
        point=svmclassify(mdl,[X(i) Y(j)]);
        if point==1
            plot(X(i),Y(j),'b.')
        else
            plot(X(i),Y(j),'r.')
        end
    end
end
xlabel('Intensity')
ylabel('Symetry')
title('SVM Classification')
etest=0;
counter=0;
for i=1:size(intensityTest)
    point=[intensityTest(i) symmetryTest(i)];
    if svmclassify(mdl,point)~=Ytest(i)
        etest=etest+1;
    end
    counter=counter+1;
end
etest=etest/counter
    
