def bytt(t, i, j):
    t[i], t[j] = t[j], t[i]

def median3sort(t, v, h):
    m = (v + h) // 2  # Finn midtpunktet
    if t[v] > t[m]:
        bytt(t, v, m)
    if t[m] > t[h]:
        bytt(t, m, h)
        if t[v] > t[m]:
            bytt(t, v, m)
    return m

def splitt(t, v, h):
    iv = v
    ih = h - 1
    m = median3sort(t, v, h)  # Finn medianen
    dv = t[m]
    bytt(t, m, h - 1)  # Flytt median til nest siste posisjon

    while True:
        while iv < h and t[iv] < dv:  # Øk iv til vi finner en større verdi
            iv += 1
        while ih > v and t[ih] > dv:  # Senk ih til vi finner en mindre verdi
            ih -= 1
        if iv >= ih:
            break
        bytt(t, iv, ih)

    bytt(t, iv, h - 1)
    return iv

def quicksort(t, v, h):
    if h - v > 2:
        delepos = splitt(t, v, h)
        quicksort(t, v, delepos - 1)
        quicksort(t, delepos + 1, h)
    else:
        median3sort(t, v, h)

# Test koden
arr = [24, 8, 42, 75, 29, 77, 38, 57]
quicksort(arr, 0, len(arr) - 1)

print('Sorted array: ', arr)
