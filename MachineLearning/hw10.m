clear
load train.csv
st=310;
nd=650;
i = train(:,2);
s = train(:,3);
class = train(:,1);
n=size(train,1);
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
min=1;
index=1;
for i=1:20
    mdl = ClassificationKNN.fit(X,Y,'NumNeighbors',i);
    cvmdl = crossval(mdl,'kfold',11);
    error(i) = kfoldLoss(cvmdl);
    if min>error(i)
        min=error(i);
        index=i;
    end
end
figure; hold on
plot(error, 'bo')
ylabel('Cross Validation Error')
xlabel('K Value')
title('Cross validation Error of KNN')
index
mdl = ClassificationKNN.fit(X,Y,'NumNeighbors',index);
cvmdl = crossval(mdl,'kfold',11);
inerror=0;
counter=0;
for i=1:300
    point=[intensity(st:nd) symmetry(st:nd)];
    if predict(mdl,point)~=Y(i)
        inerror=inerror+1;
    end
    counter=counter+1;
end
inerror=inerror/counter
crossValidationErr=kfoldLoss(cvmdl)
intensityTest=intensity(nd:end);
symmetryTest=symmetry(nd:end);
Ytest=class(nd:end);
n=size(Ytest);
figure;hold on
X = [-1:.01:1];
Y = [-1:.01:1];
for i=1:size(X,2)
    for j=1:size(Y,2)
        point=[X(i) Y(j)];
        if predict(mdl, point)==1
            plot(X(i),Y(j),'b.')
        else
            plot(X(i),Y(j),'r.')
        end
    end
end
xlabel('Intensity')
ylabel('Symetry')
title('KNN Classification')
etest=0;
counter=0;
for i=1:size(intensityTest)
    point=[intensityTest(i) symmetryTest(i)];
    if predict(mdl,point)~=Ytest(i)
        etest=etest+1;
    end
    counter=counter+1;
end
etest=etest/counter

           