function hexdump(buffer, blockSize) {
  blockSize = blockSize || 16
  const lines = []
  const hex = '0123456789ABCDEF'
  for (let b = 0; b < buffer.length; b += blockSize) {
    const block = buffer.slice(b, Math.min(b + blockSize, buffer.length))
    const addr = ('0000' + b.toString(16)).slice(-4)
    const codes = block.split('').map(function(ch) {
      const code = ch.charCodeAt(0)
      return ' ' + hex[(0xF0 & code) >> 4] + hex[0x0F & code]
    }).join('')
    let ncode = ''
    barr = codes.split(' ')
    // delete first element
    barr.shift()
    for (let i = 0; i < barr.length; i++) {
      if (i % 2 == 0 && i != 0) {
        ncode += ' '
      }
      ncode += barr[i]
    }
    ncode += '   '.repeat(blockSize - block.length)
    lines.push(addr + ' ' + ncode)
  }
  return lines.join('\n')
}

module.exports = {
  hexdump
}