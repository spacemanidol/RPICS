function multivar
a={1 2 1;0 1 -1;1 0 0;2 2 1};
gschmidt(a)

end 

function [Q,R] = gschmidt(V)
    [m,n]=size(V);
    R=zeros(n);
    R(1,1)=norm(V(:,1));
    Q(:,1)=V(:,1)/R(1,1);
    for k=2:n
        R(1:k-1,k)=Q(:,1:k-1)'*V(:,k);
        Q(:,k)=V(:,k)-Q(:,1:k-1)*R(1:k-1,k);
        R(k,k)=norm(Q(:,k));
        Q(:,k)=Q(:,k)/R(k,k);
    end 
end
