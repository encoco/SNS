import React, { useState } from 'react';

export default function ImageSlider({ imgpath }) {
  const [currentIndex, setCurrentIndex] = useState(0);
  const images = imgpath.split('|');

  const goToPrevious = () => {
    if (images.length > 1) {
      const isFirstImage = currentIndex === 0;
      const newIndex = isFirstImage ? images.length - 1 : currentIndex - 1;
      setCurrentIndex(newIndex);
    }
  };

  const goToNext = () => {
    if (images.length > 1) {
      const isLastImage = currentIndex === images.length - 1;
      const newIndex = isLastImage ? 0 : currentIndex + 1;
      setCurrentIndex(newIndex);
    }
  };

  return (
    <div className="relative w-full flex justify-center items-center overflow-hidden">
      {images.length > 1 && (
        <button onClick={goToPrevious} className="absolute left-0 z-10 inset-y-0 m-auto bg-transparent text-black text-3xl">
          &#x276E; {/* Left Arrow */}
        </button>
      )}
      <div className="w-full h-auto flex justify-center items-center">
        <img
          src={images[currentIndex]}
          alt={`Slide ${currentIndex + 1}`}
          className="w-full h-auto bg-gray-300"
          style={{ height: '500px', aspectRatio: '300/200', objectFit: 'cover' }}
        />
      </div>
      {images.length > 1 && (
        <button onClick={goToNext} className="absolute right-0 z-10 inset-y-0 m-auto bg-transparent text-black text-3xl">
          &#x276F; {/* Right Arrow */}
        </button>
      )}
    </div>
  );
}
