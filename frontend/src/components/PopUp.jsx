import "../styles/PopUp.css";

function Popup ({ message, type, onClose   }){
    return (
        <div className={`popup ${type}`}>
            <p>{message}</p>
            <button onClick={onClose} className="popup-close">
                x
            </button>
        </div>
    );
};

export default Popup;