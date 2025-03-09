document.addEventListener("alpine:init", () => {
  Alpine.data("signupForm", () => ({
    barcode: "",
    barcodeType: "",
    errors: {},
    barcodeTypes: ["UPC-A", "EAN13", "CODE128", "PDF417"],
    generatorError: false,
    generatorErrorMessage: '',

    validateForm() {
      this.errors = {};

      if (this.barcode.length < 1) {
        this.errors.barcode = "Barcode must be at least 1 character.";
      }
      if (!this.barcodeType) {
        this.errors.barcodeType = "Please select a barcode type.";
      } else {
        switch (this.barcodeType) {
            case "UPC-A":
                if (this.barcode.length != 12 && this.barcode.length != 13) {
                  this.errors.barcode = "Barcode must be 12 or 13 characters.";
                }
                break;
            case "EAN13":

            case "CODE128":
            case "PDF417":
                break;
        }
      }
    },

    submitForm($event) {
      this.validateForm();

      if (Object.keys(this.errors).length === 0) {

        this.generatorErrorMessage = ''

        fetch(`/barcodes/zx/ean13/${this.barcode}`)
        .then(response => {
            if (!response.ok) {
                response.json().then(errorData => {
                  this.generatorErrorMessage = `Error: ${errorData.error} with message: ${errorData.message}`;
                });

                this.generatorError = true;

                throw new Error('Failed to download file');
            }
            return response.blob();
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'download.png';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            window.URL.revokeObjectURL(url);
        })
        .catch(error => console.error('Error downloading file:', error));

        $event.target.reset();
      }
    },
  }));
});
