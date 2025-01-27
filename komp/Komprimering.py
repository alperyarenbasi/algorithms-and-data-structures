import struct
from collections import defaultdict, Counter
import heapq

class HuffmanNode:
    def __init__(self, char=None, freq=0):
        self.char = char
        self.freq = freq
        self.left = None
        self.right = None

    def __lt__(self, other):
        return self.freq < other.freq

def read_file_as_bytes(filename):
    """Leser en fil og returnerer innholdet som et array av bytes."""
    try:
        with open(filename, 'rb') as file:
            data = file.read()
        return data
    except FileNotFoundError:
        print("Filen ble ikke funnet. Sjekk filnavnet og plasseringen.")
        return None
    except Exception as e:
        print(f"En feil oppstod: {e}")
        return None

def lz77_compress(data, window_size=4096, lookahead_buffer_size=18):
    """Komprimerer data ved bruk av LZ77-algoritmen."""
    compressed_data = []
    i = 0

    while i < len(data):
        match_length = 0
        match_distance = 0
        lookahead_buffer = data[i:i + lookahead_buffer_size]

        for j in range(max(0, i - window_size), i):
            k = 0
            while k < len(lookahead_buffer) and j + k < len(data) and data[j + k] == lookahead_buffer[k]:
                k += 1
            if k > match_length:
                match_length = k
                match_distance = i - j

        if match_length > 1:
            if i + match_length < len(data):
                next_char = data[i + match_length]
            else:
                next_char = 0  # Indikerer slutten av data
            compressed_data.append((match_distance, match_length, next_char))
            i += match_length + 1
        else:
            compressed_data.append((0, 0, data[i]))
            i += 1

    return compressed_data

def serialize_huffman_tree(node):
    """Serialiserer Huffman-treet for lagring i en fil."""
    if node is None:
        return []
    if node.char is not None:
        return [1, node.char]  # 1 indikerer et bladnode
    return [0] + serialize_huffman_tree(node.left) + serialize_huffman_tree(node.right)

def write_compressed_file(filename, serialized_tree, huffman_data):
    """Skriver Huffman-komprimerte data, inkludert serialisert tre, til en fil."""
    try:
        with open(filename, 'wb') as file:
            # Skriv størrelsen på det serialiserte treet
            file.write(len(serialized_tree).to_bytes(2, 'big'))
            # Skriv det serialiserte treet
            file.write(bytes(serialized_tree))
            # Skriv Huffman-komprimerte data
            file.write(huffman_data)
        print(f"Komprimert data skrevet til {filename}")
    except Exception as e:
        print(f"En feil oppstod under skriving: {e}")

def build_huffman_tree(frequencies):
    heap = [HuffmanNode(char, freq) for char, freq in frequencies.items()]
    heapq.heapify(heap)

    while len(heap) > 1:
        node1 = heapq.heappop(heap)
        node2 = heapq.heappop(heap)
        merged = HuffmanNode(None, node1.freq + node2.freq)
        merged.left = node1
        merged.right = node2
        heapq.heappush(heap, merged)

    return heap[0]

def generate_huffman_codes(node, code="", code_map={}):
    if node is not None:
        if node.char is not None:
            code_map[node.char] = code
        generate_huffman_codes(node.left, code + "0", code_map)
        generate_huffman_codes(node.right, code + "1", code_map)
    return code_map

def huffman_compress(data):
    # Bygg frekvenstabellen
    frequencies = Counter(data)
    root = build_huffman_tree(frequencies)
    huffman_codes = generate_huffman_codes(root)

    # Lag en bitstreng for den komprimerte dataen
    compressed_data = "".join(huffman_codes[byte] for byte in data)

    # Konverter bitstrengen til bytes for å skrive til fil
    padded_data = compressed_data + '0' * ((8 - len(compressed_data) % 8) % 8)  # Padding for å få en hel byte
    byte_array = bytearray()
    for i in range(0, len(padded_data), 8):
        byte_array.append(int(padded_data[i:i + 8], 2))

    # Serialiser Huffman-treet
    serialized_tree = serialize_huffman_tree(root)

    return byte_array, serialized_tree

# Hovedprogram for komprimering
file_path = 'diverse.lyx'  # Erstatt med filnavnet du vil lese
output_path = 'div_comp.lyx'  # Navnet på den komprimerte filen
file_data = read_file_as_bytes(file_path)

if file_data:
    # Huffman-komprimering av input-data
    huffman_compressed, serialized_tree = huffman_compress(file_data)

    # Skrive den komprimerte dataen til en fil
    write_compressed_file(output_path, serialized_tree, huffman_compressed)

    print("Kombinert komprimering fullført!")