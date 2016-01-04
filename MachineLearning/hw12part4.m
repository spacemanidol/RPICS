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
   C=.1;
   min=1;
   error=1;
   index=1;
for i=1:100
    c = cvpartition(Y,'kfold',10);
    X_Train = X(training(c,1),:);
    Y_Train = Y(training(c,1));
    X_Test=X(test(c,1),:);
    Y_Test=Y(test(c,1));
    svmModel = svmtrain(X_Train, Y_Train, 'Kernel_Function','polynomial','polyorder',5,'BoxConstraint',C);
    X_Test=svmclassify(svmModel,X_Test);
    error(i) =abs(((sum((X_Test-Y_Test)/2))/size(X_Test,1)));
    if min>error(i)
        min=error(i);
        index=i;
    end
    C=C+.010;
end
figure; hold on
plot(error, 'bo')
ylabel('Cross Validation Error')
xlabel('C*100')
title('Cross validation Error of C for SVM ')
index
min
c=.95
svmModel = svmtrain(X_Train, Y_Train, 'Kernel_Function','polynomial','polyorder',5,'BoxConstraint',C);
Xtest=svmclassify(svmModel,Xtest);
    %mdl = svmtrain(X,Y,'Kernel_Function','polynomial','polyorder',5,'BoxConstraint',C);

    figure;hold on;
X = [-1:.01:1];
Y = [-1:.01:1];
for i=1:size(X,2)
    for j=1:size(Y,2)
       % point=svmclassify(mdl,[X(i) Y(j)]);
        if point==1
           % plot(X(i),Y(j),'b.')
        else
            %plot(X(i),Y(j),'r.')
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
    
