import heapq
import os
from collections import defaultdict, Counter

# --- LZW Compression and Decompression on Byte-Level ---

def lzw_compress(data):
    dictionary = {bytes([i]): i for i in range(256)}
    next_code = 256
    current_sequence = bytes()
    compressed_data = []

    for byte in data:
        sequence_plus_char = current_sequence + bytes([byte])
        if sequence_plus_char in dictionary:
            current_sequence = sequence_plus_char
        else:
            compressed_data.append(dictionary[current_sequence])
            dictionary[sequence_plus_char] = next_code
            next_code += 1
            current_sequence = bytes([byte])

    if current_sequence:
        compressed_data.append(dictionary[current_sequence])

    return compressed_data


def lzw_decompress(compressed_data):
    dictionary = {i: bytes([i]) for i in range(256)}
    next_code = 256
    current_code = compressed_data.pop(0)
    decompressed_data = bytearray(dictionary[current_code])
    current_sequence = dictionary[current_code]

    for code in compressed_data:
        if code in dictionary:
            entry = dictionary[code]
        elif code == next_code:
            entry = current_sequence + current_sequence[:1]
        else:
            raise ValueError("Invalid LZW code encountered.")

        decompressed_data.extend(entry)
        dictionary[next_code] = current_sequence + entry[:1]
        next_code += 1
        current_sequence = entry

    return bytes(decompressed_data)

# --- Huffman Encoding and Decoding ---

class HuffmanNode:
    def __init__(self, char=None, freq=0, left=None, right=None):
        self.char = char
        self.freq = freq
        self.left = left
        self.right = right

    def __lt__(self, other):
        return self.freq < other.freq

def build_huffman_tree(frequencies):
    heap = [HuffmanNode(char, freq) for char, freq in frequencies.items()]
    heapq.heapify(heap)

    while len(heap) > 1:
        left = heapq.heappop(heap)
        right = heapq.heappop(heap)
        merged = HuffmanNode(freq=left.freq + right.freq, left=left, right=right)
        heapq.heappush(heap, merged)

    return heap[0]

def build_codes(node, prefix="", codebook={}):
    if node:
        if node.char is not None:
            codebook[node.char] = prefix
        build_codes(node.left, prefix + "0", codebook)
        build_codes(node.right, prefix + "1", codebook)
    return codebook

def huffman_compress(data):
    frequencies = Counter(data)
    root = build_huffman_tree(frequencies)
    codebook = build_codes(root)

    # Encode the data as bits
    encoded_data = ''.join(codebook[byte] for byte in data)
    return encoded_data, codebook

def huffman_decompress(encoded_data, codebook):
    reverse_codebook = {v: k for k, v in codebook.items()}
    decoded_data = bytearray()
    current_code = ""
    for bit in encoded_data:
        current_code += bit
        if current_code in reverse_codebook:
            # Add the byte value (as an integer) to decoded_data
            decoded_data.append(reverse_codebook[current_code][0])
            current_code = ""
    return bytes(decoded_data)


# --- Full Compression and Decompression ---

def compress_file(input_path, output_path):
    # Read the input file as bytes
    with open(input_path, 'rb') as file:
        data = file.read()

    # Step 1: LZW compression
    lzw_compressed = lzw_compress(data)

    # Convert LZW output to bytes for Huffman compression
    lzw_compressed_bytes = bytearray()
    for code in lzw_compressed:
        lzw_compressed_bytes.extend(code.to_bytes(3, byteorder='big'))  # Using 3 bytes for each code

    # Step 2: Huffman compression
    huffman_compressed, codebook = huffman_compress(lzw_compressed_bytes)

    # Convert the bit string to bytes
    huffman_compressed_bytes = bytearray()
    for i in range(0, len(huffman_compressed), 8):
        byte = huffman_compressed[i:i+8]
        huffman_compressed_bytes.append(int(byte, 2))

    # Save the compressed data and the Huffman codebook
    with open(output_path, 'wb') as file:
        # Save codebook length and data length for reconstruction
        file.write(len(codebook).to_bytes(2, byteorder='big'))
        file.write(len(huffman_compressed_bytes).to_bytes(4, byteorder='big'))

        # Save the codebook (character: code) as bytes
        for char, code in codebook.items():
            file.write(char.to_bytes(1, byteorder='big'))
            file.write(len(code).to_bytes(1, byteorder='big'))
            file.write(int(code, 2).to_bytes((len(code) + 7) // 8, byteorder='big'))

        # Save compressed Huffman data
        file.write(huffman_compressed_bytes)


def decompress_file(input_path, output_path):
    with open(input_path, 'rb') as file:
        # Load codebook length and data length
        codebook_len = int.from_bytes(file.read(2), byteorder='big')
        huffman_data_len = int.from_bytes(file.read(4), byteorder='big')

        # Load the Huffman codebook
        codebook = {}
        for _ in range(codebook_len):
            char = file.read(1)
            code_len = int.from_bytes(file.read(1), byteorder='big')
            code = bin(int.from_bytes(file.read((code_len + 7) // 8), 'big'))[2:].zfill(code_len)
            codebook[char] = code

        # Load the Huffman compressed data
        huffman_compressed_bytes = file.read(huffman_data_len)

    # Convert bytes to bit string
    huffman_compressed = ''.join(f'{byte:08b}' for byte in huffman_compressed_bytes)

    # Step 1: Huffman decompression
    lzw_compressed_bytes = huffman_decompress(huffman_compressed, codebook)

    # Step 2: LZW decompression
    lzw_compressed = [int.from_bytes(lzw_compressed_bytes[i:i+3], byteorder='big') for i in range(0, len(lzw_compressed_bytes), 3)]
    decompressed_data = lzw_decompress(lzw_compressed)

    # Write the decompressed data to the output file
    with open(output_path, 'wb') as file:
        file.write(decompressed_data)


# Example usage:
#compress_file('f3Wiki.txt', 'f3compressed.bin')
decompress_file('diverse.lyx', 'diverse_decompressed.lyx')
