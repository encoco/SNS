import React, { useState } from 'react';

const DropdownMenu = ({ onEdit, onDelete }) => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className="relative">
      <button onClick={() => setIsOpen(!isOpen)} className="text-gray-800 p-2">
        ⋮
      </button>
      {isOpen && (
        <div className="absolute right-0 w-40 mt-2 bg-white border rounded shadow-lg">
          <ul className="text-gray-700">
            <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer" onClick={onEdit}>Edit</li>
            <li className="px-4 py-2 hover:bg-gray-100 cursor-pointer" onClick={onDelete}>Delete</li>
          </ul>
        </div>
      )}
    </div>
  );
};

export default DropdownMenu;