import os
import heapq
from collections import Counter
from bitarray import bitarray
import pickle

# LZW Compression

def lzw_compress(data, max_table_size=4096):
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
            if len(dictionary) < max_table_size:
                dictionary[sequence_plus_char] = next_code
                next_code += 1
            current_sequence = bytes([byte])

    if current_sequence:
        compressed_data.append(dictionary[current_sequence])

    return compressed_data


# Huffman Encoding with bitarray

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

def build_codes(node, prefix=bitarray(), codebook={}):
    if node:
        if node.char is not None:
            codebook[node.char] = prefix.copy()
        build_codes(node.left, prefix + bitarray('0'), codebook)
        build_codes(node.right, prefix + bitarray('1'), codebook)
    return codebook

def huffman_compress(data):
    frequencies = Counter(data)
    root = build_huffman_tree(frequencies)
    codebook = build_codes(root)

    encoded_data = bitarray()
    for byte in data:
        encoded_data.extend(codebook[byte])

    return encoded_data, codebook


# Full Compression

def compress_file(input_path):
    # Genere output filnavn
    base, ext = os.path.splitext(input_path)
    output_path = f"{base}_compressed{ext}"

    with open(input_path, 'rb') as file:
        data = file.read()

    lzw_compressed = lzw_compress(data)
    max_code = max(lzw_compressed)
    code_size = (max_code.bit_length() + 7) // 8

    lzw_compressed_bytes = bytearray()
    for code in lzw_compressed:
        lzw_compressed_bytes.extend(code.to_bytes(code_size, byteorder='big'))


    huffman_compressed, codebook = huffman_compress(lzw_compressed_bytes)

    # Save compressed data and Huffman codebook using pickle
    with open(output_path, 'wb') as file:
        pickle.dump({
            'code_size': code_size,
            'codebook': {k: v.to01() for k, v in codebook.items()},
            'compressed_data': huffman_compressed.tobytes(),
            'data_bit_length': len(huffman_compressed)
        }, file)

    # fil strl
    original_size = os.path.getsize(input_path)
    compressed_size = os.path.getsize(output_path)
    reduction_percentage = ((compressed_size / original_size)) * 100

    # Print resultat
    print(f"Compressed '{input_path}' to '{output_path}'")
    print(f"Original file size: {original_size} bytes")
    print(f"Compressed file size: {compressed_size} bytes")
    print(f"Compression reduced the file size to {reduction_percentage:.2f}% of the original.")

# Example
#compress_file('diverse.lyx')


# Terminalkjøring
if __name__ == "__main__":
    import sys
    if len(sys.argv) != 2:
        print("Bruk: python compress.py <input_fil>")
    else:
        compress_file(sys.argv[1])

