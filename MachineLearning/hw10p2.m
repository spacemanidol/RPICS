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
figure; hold on
min=1;
index=2;
for k=1:25
    Z=[ones(n,1) zeros(n,k)];
    r=2/(sqrt(k));
    [idx,ctrs]=kmeans(X,k);
    for j=1:k
        for n=1:size(X,1)
          D=sqrt((X(n,1)-ctrs(j,1))^2+(X(n,2)-ctrs(j,2))^2);
          Z(n,j+1)=exp(-.5*(D/r)^2);
        end
    end
    w=((Z'*Z)^(-1)*Z');
    w=w*Y;
    g=sign(Z*w);
    error=0;
    for i=1:n
       if g(i)~=Y(i)
       error=error+1;
       end
    end
    ein=error/n;
    plot(k,ein,'bx')
    if min>ein
        min=ein;
        index=k;
    end
end
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
 g=sign(Z*w);
 inerror=sum(g-Y)/(2*size(g,1))
 
 X=[zeros(40000,2)];
 
 x=-1;
 c=1;
 for i=1:201
 y=-1;
     for j=1:201
        X(c,:)=[x y];
        c=c+1;
     y=y+.01;
     end
 x=x+.01;
 end
 
 n=size(X,1);
 Z=[ones(n,1) zeros(n,k)];

 for j=1:k
        for n=1:size(X,1)
          D=sqrt((X(n,1)-ctrs(j,1))^2+(X(n,2)-ctrs(j,2))^2);
          Z(n,j+1)=exp(-.5*(D/r)^2);
        end
 end
 
g=sign(Z*w);
figure; hold on
for i=1:size(X,1)
        if g(i)==1
            plot(X(i,1),X(i,2),'b.')
        else
            plot(X(i,1),X(i,2),'r.')
        end
end





