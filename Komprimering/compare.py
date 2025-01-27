def compare_files(file1_path, file2_path):
    """Sammenligner to filer linje for linje og printer ut forskjellene."""
    try:
        with open(file1_path, 'r', encoding='utf-8') as file1, open(file2_path, 'r', encoding='utf-8') as file2:
            file1_lines = file1.readlines()
            file2_lines = file2.readlines()

        max_lines = max(len(file1_lines), len(file2_lines))
        differences_found = False

        for i in range(max_lines):
            line1 = file1_lines[i].strip() if i < len(file1_lines) else "<ingen linje>"
            line2 = file2_lines[i].strip() if i < len(file2_lines) else "<ingen linje>"

            if line1 != line2:
                print(f"Forskjell på linje {i + 1}:")
                print(f"Fil 1: {line1}")
                print(f"Fil 2: {line2}")
                print()
                differences_found = True

        if not differences_found:
            print("Filene er identiske.")
    except FileNotFoundError as e:
        print(f"Feil: {e}")
    except Exception as e:
        print(f"En feil oppstod: {e}")

# Eksempel på bruk
compare_files('diverse.lyx', 'diverse_decompressed.lyx')
