

import sys

print('Number of arguments:', len(sys.argv), 'arguments.')
print('Argument List:', str(sys.argv))

print("sum = ", sum(map(int, sys.argv[1:])))

return sum(map(int, sys.argv[1:]))