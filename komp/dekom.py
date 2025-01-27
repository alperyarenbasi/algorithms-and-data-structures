import struct
import heapq

class HuffmanNode:
    def __init__(self, char=None, freq=0):
        self.char = char
        self.freq = freq
        self.left = None
        self.right = None

    def __lt__(self, other):
        return self.freq < other.freq

def read_compressed_file(filename):
    """Leser komprimert data fra en fil og returnerer innholdet som bytes."""
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

def rebuild_huffman_tree_recursive(serialized_tree, index=0):
    """Rekursiv funksjon for å gjenoppbygge Huffman-treet fra en serialisert representasjon."""
    if index >= len(serialized_tree):
        return None, index

    if serialized_tree[index] == 1:  # Indikerer et bladnode
        index += 1
        if index < len(serialized_tree):
            char = serialized_tree[index]
            return HuffmanNode(char=char), index + 1
        else:
            print("Feil: Ugyldig serialized tree-format ved bladnode.")
            return None, index
    else:  # Indikerer en intern node
        left_node, next_index = rebuild_huffman_tree_recursive(serialized_tree, index + 1)
        right_node, next_index = rebuild_huffman_tree_recursive(serialized_tree, next_index)
        node = HuffmanNode()
        node.left = left_node
        node.right = right_node
        return node, next_index

def huffman_decompress(compressed_data):
    """Dekomprimerer data ved bruk av Huffman-algoritmen."""
    serialized_tree_size = int.from_bytes(compressed_data[:2], 'big')
    serialized_tree = list(compressed_data[2:2 + serialized_tree_size])
    compressed_bits = compressed_data[2 + serialized_tree_size:]

    #print(f"Serialized tree size: {serialized_tree_size}")
    #print(f"Serialized tree: {serialized_tree}")

    root, _ = rebuild_huffman_tree_recursive(serialized_tree)
    if not root:
        print("Feil: Huffman-treet kunne ikke gjenoppbygges.")
        return None

    bit_string = "".join(f"{byte:08b}" for byte in compressed_bits)

    # Fjerne eventuell padding ved slutten av bitstrengen
    end_padding_index = bit_string.rfind('1') + 1
    if end_padding_index > 0:
        bit_string = bit_string[:end_padding_index]

    current_node = root
    decompressed_data = bytearray()
    for bit in bit_string:
        current_node = current_node.left if bit == '0' else current_node.right

        if current_node.left is None and current_node.right is None:  # Er et bladnode
            decompressed_data.append(current_node.char)
            current_node = root

    return decompressed_data

def write_decompressed_file(filename, data):
    """Skriver dekomprimert data til en fil."""
    try:
        with open(filename, 'wb') as file:
            file.write(data)
        print(f"Dekomprimering fullført! Lagret som '{filename}'.")
    except Exception as e:
        print(f"En feil oppstod ved skriving av dekomprimert fil: {e}")

# Hovedprogram for dekomprimering
input_path = 'f2_compressed.txt'  # Navn på den komprimerte filen
output_path = 'f2_dec.txt'  # Navn på den dekomprimerte filen

compressed_data = read_compressed_file(input_path)
if compressed_data:
    decompressed_data = huffman_decompress(compressed_data)
    if decompressed_data:
        write_decompressed_file(output_path, decompressed_data)
