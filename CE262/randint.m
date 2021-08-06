function n = randint(N)

% Generates random integers between 1 and N
% from the uniform probability mass function

n = 0;
a = 0.0;
x = rand();  % x is a real-valued random number,
             % uniformly distributed between 0 and 1
while a <= x
    n = n+1;
    a = a+1/N;
end
