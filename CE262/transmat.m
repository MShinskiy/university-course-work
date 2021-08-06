function PI = transmat(p, M)

%  Returns an (M+1)x(M+1) transition matrix for
%    the queue length process X.
%  p is the packet arrival probability for each
%    of the two incoming links.

A = zeros(M+1);

%  The next two lines set the values for the first row
A(1,1) = 1-p^2;
A(1,2) = p^2;

%  The next for loop deals with rows 2 to M
for m=2:M
    A(m,m-1) = (1-p)^2;
    A(m,m) = 2*p*(1-p);
    A(m,m+1) = p^2;
end

%  The next two lines set the values for the last row
A(M+1,M) = (1-p)^2;
A(M+1,M+1) = 1-(1-p)^2;

PI = A;


