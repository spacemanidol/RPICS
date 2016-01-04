clear
load train.csv
st=300;
nd=600;
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
n=nd-st;

k=18;
r=2/(sqrt(k));
Z=[ones(n,1) zeros(n,k)];
[idx,ctrs]=kmeans(X,k);
    for j=1:k
        for n=1:size(X,1)
          D=sqrt((X(n,1)-ctrs(j,1))^2+(X(n,2)-ctrs(j,2))^2);
          Z(n,j+1)=exp(-.5*(D/r)^2);
        end
    end
 w=((Z'*Z)^(-1)*Z');
 w=w*Y;
 
 X=[intensity(601:end) symmetry(601:end)];
Y=class(601:end);
n=size(X);
 Z=[ones(n,1) zeros(n,k)];

 for j=1:k
        for n=1:size(X,1)
          D=sqrt((X(n,1)-ctrs(j,1))^2+(X(n,2)-ctrs(j,2))^2);
          Z(n,j+1)=exp(-.5*(D/r)^2);
        end
 end
 
g=sign(Z*w);
figure; hold on
error=0;
for i=1:size(X,1)
        if g(i)~=Y(i)
           error=error+1;
           plot(X(i,1),X(i,2),'ro','LineWidth',2)
        else
            if g(i)==1
                plot(X(i,1),X(i,2),'kx')
            else
                plot(X(i,1),X(i,2),'ko')
            end
        end
end
error=error/size(g,1)


