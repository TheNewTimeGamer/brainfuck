# brainfuck
A brainfuck interpreter with a compressor halving file size.

Compressor:
Brainfuck only uses 8 operators:
< > , . + - [ ]

Each operator is a char, each char is a byte.
A byte can represent 256 different values, yet brainfuck only uses 8.

Lets take this very simple program '++[-].' .
This program will loop twice and print '0'.

The binary representation of this program is (ASCII):
00101011 00101011 01011011 00101101 01011101 00101110

Now we don't want our values to be this high, so we create our own encoding for our brainfuck code, lets take:

< = 0 (0000)
> = 1 (0001)
, = 2 (0010)
. = 3 (0011)
+ = 4 (0100)
- = 5 (0101)
[ = 6 (0110)
] = 7 (0111)

Any order will do, this is just an example.

Now you might ask: "we still have an unused bit!".
Yes we do, we'll get to that.

So, our program - using our own encoding - becomes:
00000100 00000100 00000110 00000101 00000111 00000011

They are the same size, but now we have a bunch of zeros as padding, which we can utilize.
The compressor bit shifts the (i*2) value into the (i) value, where 'i' stands for the current index (increments of 2) in the byte array.

So the program becomes:

00000100 | 00000100 << 4 = 01000100

00000110 | 00000101 << 4 = 01010110

00000111 | 00000011 << 4 = 00110111

01000100 01010110 00110111

I.E: half its previous size.

Now about the unused bit:
If we were to take the current setup we would only need 3 bits per operator.
This would result in only needing 6 bits per byte, so we could make the program 37.5% its original size!
Of course, because we would be splitting the operators between bytes we would for a program whos length is not to the power of 6 also need to add one padding byte, which contains the remaining half of the last operator.
Still, an amazing result!

Now, I might support this amazing 37.5% at a later date if I feel like it (Make it an optional parameter).
But currently I am quite content and mainly because a lot of brainfuck implementations use an extra operator.
The most notable of which is an EOF-operator.
Thats why this last bit is not used, just in case somebody wants to compress a brainfuck program which had more than 8 operators.

