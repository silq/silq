f = open('qualis.txt', 'r')

c = 0
b = []
for line in f.readlines():
    c += 1
    b.append(line.replace('\r\n', ''))
    # print b

    if c % 3 == 0:
        print('\t'.join(b))
        c = 0
        b = []
