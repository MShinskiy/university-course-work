nA = 0;      % Initialise all
nD = 0;      % . . occurrence counts
nAD = 0;     % . . to zero

for m = 1:Mrep
    Red = randint(6);       % Simulate
    Blue = randint(6);      % . . dice rolls
    if (Red<4 && Blue<4)    % If event A occurs
        nA = nA + 1;        % . . increment event A counter, and
        if (Red==Blue)      % . . if, in addition, event D occurs
            nAD = nAD + 1;  % . . increment event (A AND D) counter
        end
    end
    if (Red==Blue)    % If event D occurs
        nD = nD + 1;  % . . increment event D counter
    end
end

rfA = nA/Mrep;    % Scale all
rfD = nD/Mrep;    % . . occurrence counts
rfAD = nAD/Mrep;  % . . to find relative frequencies