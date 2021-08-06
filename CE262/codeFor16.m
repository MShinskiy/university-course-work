M = 5;
T = 30;
Fz = zeros(1, M+1);
F = zeros(T, M+1);
for t = 1:T
    F(t,:) = Fz*PI^t;
end
tt = 1:T;
plot(tt, F(:, 1), '+'), grid;
