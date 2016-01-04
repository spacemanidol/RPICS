clear
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
lambda=nd-st;
lambda=.01/lambda;
Y=class(st:nd);
inputs = X';
targets = Y';
Xtest=[intensity(nd+1:end) symmetry(nd+1:end)]';
Ytest=class(nd+1:end)';
% Create a Pattern Recognition Network
hiddenLayerSize = 10;
net = patternnet(hiddenLayerSize);
net=setwb(net,rand(10,1));
%[trainInd,valInd,testInd]=dividerand(X,.83,.17,0);
% Choose Input and Output Pre/Post-Processing Functions
% For a list of all processing functions type: help nnprocess
net.inputs{1}.processFcns = {'mapminmax'};
net.outputs{2}.processFcns = {'mapminmax'};
net.divideFcn = 'dividerand';  % Divide data randomly
net.divideMode = 'sample';  % Divide up every sample
net.divideParam.trainRatio = 83/100;
net.divideParam.valRatio = 17/100;
net.divideParam.testRatio = 0/100;
net.layers{1}.transferFcn='tansig';
% For help on training function 'trainscg' type: help trainscg
% For a list of all training functions type: help nntrain
net.trainFcn = 'traingda';  % Scaled conjugate gradient
%net.trainFcn = 'traingd';
% Choose a Performance Function
% For a list of all performance functions type: help nnperformance
%net.performFcn = 'mse';  % Mean squared error
net.performFcn = 'msereg'; %weight decay
net.performParam.regularization=lambda;
net.trainParam.epochs=2*10^5;
net.trainParam.min_grad=0;
net.trainParam.lr=.25;
net.trainParam.max_fail=1500;
% Choose Plot Functions
% For a list of all plot functions type: help nnplot
net.plotFcns = {'plotperform','plottrainstate','ploterrhist', ...
  'plotregression', 'plotfit'};
% Train the Network
[net,tr] = train(net,inputs,targets);


% Test the Network
outputs = net(inputs);
testOutputs = sign(net(Xtest));
errors = gsubtract(targets,outputs);
testError= (sum(gsubtract(Ytest,testOutputs))/2);
testError=testError/size(Ytest,2)
figure; hold on
for i=1:size(Ytest,2)
    if(Ytest(:,i)==testOutputs(:,i))
        plot(Xtest(1,i),Xtest(2,i),'bo')
    else
        plot(Xtest(1,i),Xtest(2,i),'rx')
    end
end
figure; hold on
X = [-1:.01:1];
Y = [-1:.01:1];
counter=1;
for i=1:size(X,2)
    for j=1:size(Y,2)
        counter=counter+1;
    end
end
points=zeros(counter,1);
points2=zeros(counter,2);
counter=1;
for i=1:size(X,2)
    for j=1:size(Y,2)
        points2(counter,:)=[X(i) Y(j)];
        counter=counter+1;
    end
end
points=sign(net(points2'));
for i=1:size(points,2)
    if(points(1,i)==1)
         plot(points2(i,1),points2(i,2),'b.')
    else
        plot(points2(i,1),points2(i,2),'r.')
    end
end
xlabel('Intensity')
ylabel('Symetry')
trainTargets = targets .* tr.trainMask{1};
valTargets = targets  .* tr.valMask{1};
testTargets = targets  .* tr.testMask{1};
trainPerformance = perform(net,trainTargets,outputs)
valPerformance = perform(net,valTargets,outputs)
% View the Network
view(net);