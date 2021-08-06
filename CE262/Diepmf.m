occur = zeros(1,6);          % Initialise 6-vector of outcome counters

for m = 1:Mrep
    x = randint(6);          % Simulate die roll
    occur(x) = occur(x) + 1; % Increment counter corresponding to outcome
end

relfreq = occur/Mrep;        % Scale the occurrence counters to find
                             % . . 6-vector of relative frequencies
plot(relfreq, '*'), grid;    % Plot relative frequencies
xlabel("score");
ylabel("rf");