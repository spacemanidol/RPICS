x=[1; 1];
y=[1];
z=[1];
% Solve a Pattern Recognition Problem with a Neural Network
% Script generated by NPRTOOL
% Created Wed Dec 04 21:34:05 EST 2013
%
% This script assumes these variables are defined:
%
%   x - input data.
%   y - target data.

inputs = x;
targets = y;

% Create a Pattern Recognition Network
hiddenLayerSize = 2;
net = patternnet(hiddenLayerSize);
weight=[.25;.25];
net=setwb(net,weight);
% Choose Input and Output Pre/Post-Processing Functions
% For a list of all processing functions type: help nnprocess
net.inputs{1}.processFcns = {'mapminmax'};
net.outputs{2}.processFcns = {'mapminmax'};
net.layers{1}.transferFcn='tansig';
%net.layers{1}.transferFnc='identity';

% For help on training function 'trainscg' type: help trainscg
% For a list of all training functions type: help nntrain
net.trainFcn = 'traingd';  % Scaled conjugate gradient

% Choose a Performance Function
% For a list of all performance functions type: help nnperformance
net.performFcn = 'mse';  % Mean squared error

% Choose Plot Functions
% For a list of all plot functions type: help nnplot
net.plotFcns = {'plotperform','plottrainstate','ploterrhist', ...
  'plotregression', 'plotfit'};


% Train the Network
[net,tr] = train(net,inputs,targets);

% Test the Network
outputs = net(inputs);
errors = gsubtract(targets,outputs);
performance = perform(net,targets,outputs)

% Recalculate Training, Validation and Test Performance
trainTargets = targets .* tr.trainMask{1};
valTargets = targets  .* tr.valMask{1};
testTargets = targets  .* tr.testMask{1};
trainPerformance = perform(net,trainTargets,outputs)
valPerformance = perform(net,valTargets,outputs)
testPerformance = perform(net,testTargets,outputs)

% View the Network
view(net)

% Plots
% Uncomment these lines to enable various plots.
%figure, plotperform(tr)
%figure, plottrainstate(tr)
%figure, plotconfusion(targets,outputs)
%figure, plotroc(targets,outputs)
%figure, ploterrhist(errors)
