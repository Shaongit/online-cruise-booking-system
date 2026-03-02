import fitz  # PyMuPDF
from PIL import Image
import io
import pytesseract
import os

try:
    pdf_document = fitz.open('docs/Assingment-2.pdf')
    print(f"Number of pages: {len(pdf_document)}")
    
    num_pages = len(pdf_document)
    
    # Try to locate Tesseract (common installation paths on Windows)
    possible_tesseract_paths = [
        r"C:\Program Files\Tesseract-OCR\tesseract.exe",
        r"C:\Program Files (x86)\Tesseract-OCR\tesseract.exe",
        r"C:\Users\Khaleda\AppData\Local\Programs\Tesseract-OCR\tesseract.exe",
    ]
    
    tesseract_found = False
    for path in possible_tesseract_paths:
        if os.path.exists(path):
            pytesseract.pytesseract.tesseract_cmd = path
            tesseract_found = True
            print(f"Found Tesseract at: {path}")
            break
    
    text = ''
    images_extracted = 0
    
    for page_num in range(len(pdf_document)):
        page = pdf_document[page_num]
        
        # Try to get text first
        page_text = page.get_text()
        
        if page_text and page_text.strip():
            print(f"Page {page_num + 1}: Extracted {len(page_text)} characters of text")
            text += f"# Page {page_num + 1}\n\n"
            text += page_text + '\n\n'
        else:
            # No text, try to extract and OCR images
            print(f"Page {page_num + 1}: No text, attempting OCR on images...")
            
            # Convert page to image
            pix = page.get_pixmap(matrix=fitz.Matrix(2, 2))  # 2x zoom for better quality
            img_data = pix.tobytes("png")
            image = Image.open(io.BytesIO(img_data))
            images_extracted += 1
            
            if tesseract_found:
                try:
                    # Perform OCR
                    ocr_text = pytesseract.image_to_string(image)
                    if ocr_text and ocr_text.strip():
                        print(f"  ✓ OCR extracted {len(ocr_text)} characters")
                        text += f"# Page {page_num + 1}\n\n"
                        text += ocr_text + '\n\n'
                    else:
                        print(f"  ✗ OCR did not extract any text")
                except Exception as e:
                    print(f"  ✗ OCR failed: {e}")
            else:
                print(f"  → Tesseract not found, skipping OCR")
    
    pdf_document.close()
    
    # Write results
    if text.strip():
        with open('docs/Requirement.md', 'w', encoding='utf-8') as f:
            f.write(text)
        print(f"\n✓ Successfully written to docs/Requirement.md")
        print(f"Total characters extracted: {len(text)}")
    else:
        print(f"\n✗ No text could be extracted from the PDF")
        if not tesseract_found:
            print("\n⚠ Tesseract OCR is not installed!")
            print("\nTo extract text from this scanned PDF, please:")
            print("1. Download Tesseract OCR from: https://github.com/UB-Mannheim/tesseract/wiki")
            print("2. Install it to one of these locations:")
            for path in possible_tesseract_paths:
                print(f"   - {path}")
            print("3. Run this script again")
        
        # Create a placeholder file
        with open('docs/Requirement.md', 'w', encoding='utf-8') as f:
            f.write("# Requirements\n\n")
            f.write("*Note: This is a placeholder. The PDF contains scanned images that require OCR.*\n\n")
            f.write(f"The PDF has {num_pages} pages with scanned content.\n\n")
            f.write("Please either:\n")
            f.write("1. Install Tesseract OCR and run the extraction script again\n")
            f.write("2. Manually transcribe the content from the PDF\n")
        print("Created placeholder file at docs/Requirement.md")
        
except Exception as e:
    print(f"Error: {e}")
    import traceback
    traceback.print_exc()
