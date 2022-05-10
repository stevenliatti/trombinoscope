INPUT_PHOTOS_DIR = photos
THUMBS_WIDTH = 200
THUMBS_DIR = thumbs
MAX_CHORIST_PER_ROW = 4
IMAGES_MARGIN = 0.7
IMAGE_EXT_FILE = JPEG
IN_DIR = in
OUT_DIR = out
DIRECTOR_NAME = Pierre-Antoine Marçais

all: convertImages trombinoscope.pdf

trombinoscope.pdf: $(IN_DIR)/trombinoscope.tex $(OUT_DIR)/data.tex
	cp $< $(OUT_DIR)
	sed -i 's/@DIRECTOR/$(DIRECTOR_NAME)/g' $(OUT_DIR)/trombinoscope.tex
	pdflatex $(OUT_DIR)/trombinoscope.tex

$(OUT_DIR)/data.tex: $(IN_DIR)/choeur-photos.csv
	mkdir -p $(OUT_DIR)
	scala src/MakeData.scala $< $(MAX_CHORIST_PER_ROW) $(IMAGES_MARGIN) $(IMAGE_EXT_FILE) $@

convertImages:
	scala src/ConvertImages.scala $(INPUT_PHOTOS_DIR) $(THUMBS_DIR) $(THUMBS_WIDTH)

mostlyclean:
	rm -rf *.aux *.log

clean: mostlyclean
	rm -rf $(OUT_DIR) $(THUMBS_DIR)

.PHONY: $(OUT_DIR)/data.tex mostlyclean clean
