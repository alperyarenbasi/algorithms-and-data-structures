from bitarray import bitarray
import pickle
import os


# LZW Decompression

def lzw_decompress(compressed_data, code_size, max_table_size=4096):
    dictionary = {i: bytes([i]) for i in range(256)}
    next_code = 256
    decompressed_data = bytearray()

    codes = []
    for i in range(0, len(compressed_data), code_size):
        code = int.from_bytes(compressed_data[i:i+code_size], byteorder='big')
        codes.append(code)

    current_code = codes.pop(0)
    current_sequence = dictionary[current_code]
    decompressed_data.extend(current_sequence)

    for code in codes:
        if code in dictionary:
            entry = dictionary[code]
        elif code == next_code:
            entry = current_sequence + current_sequence[:1]
        else:
            raise ValueError("Invalid LZW code encountered.")

        decompressed_data.extend(entry)

        if len(dictionary) < max_table_size:
            dictionary[next_code] = current_sequence + entry[:1]
            next_code += 1

        current_sequence = entry

    return bytes(decompressed_data)

# Huffman Decoding

def huffman_decompress(encoded_data, codebook, data_bit_length):
    reverse_codebook = {v.to01(): k for k, v in codebook.items()}
    decoded_data = bytearray()
    current_code = ''
    bits = bitarray()
    bits.frombytes(encoded_data)
    bits = bits[:data_bit_length]

    for bit in bits:
        current_code += '1' if bit else '0'
        if current_code in reverse_codebook:
            decoded_data.append(reverse_codebook[current_code])
            current_code = ''
    return bytes(decoded_data)


# Full Decompression


def decompress_file(input_path):
    base, ext = os.path.splitext(input_path)
    if base.endswith('_compressed'):
        base = base[:-11]  # Fjern '_compressed' (11 tegn)
    output_path = f"{base}_decompressed{ext}"

    with open(input_path, 'rb') as file:
        data = pickle.load(file)

    code_size = data['code_size']
    codebook = {k: v for k, v in data['codebook'].items()}
    compressed_data = data['compressed_data']
    data_bit_length = data['data_bit_length']

    codebook = {k: bitarray(v) for k, v in codebook.items()}

    lzw_compressed_bytes = huffman_decompress(compressed_data, codebook, data_bit_length)

    decompressed_data = lzw_decompress(lzw_compressed_bytes, code_size)

    with open(output_path, 'wb') as file:
        file.write(decompressed_data)

    print(f"Decompressed '{input_path}' to '{output_path}'")


#example
#decompress_file('diverse_compressed.lyx')

# Terminalkjøring
if __name__ == "__main__":
    import sys
    if len(sys.argv) != 2:
        print("Bruk: python decompress.py <input_fil>")
    else:
        decompress_file(sys.argv[1])

